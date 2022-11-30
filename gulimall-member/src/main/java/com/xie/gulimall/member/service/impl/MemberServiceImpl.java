package com.xie.gulimall.member.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xie.common.utils.HttpUtils;
import com.xie.gulimall.member.dao.MemberLevelDao;
import com.xie.gulimall.member.entity.MemberLevelEntity;
import com.xie.gulimall.member.exception.PhoneExistException;
import com.xie.gulimall.member.exception.UserNameExistException;
import com.xie.gulimall.member.vo.GiteeSocialUser;
import com.xie.gulimall.member.vo.MemberRegistVo;
import com.xie.gulimall.member.vo.MemberUserLoginVo;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xie.common.utils.PageUtils;
import com.xie.common.utils.Query;

import com.xie.gulimall.member.dao.MemberDao;
import com.xie.gulimall.member.entity.MemberEntity;
import com.xie.gulimall.member.service.MemberService;


@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {

    @Autowired
    MemberLevelDao memberLevelDao;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<MemberEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void register(MemberRegistVo vo) {

        MemberEntity memberEntity =new MemberEntity();

        //设置会员默认等级【普通会员】
      MemberLevelEntity levelEntity= memberLevelDao.getDefaultLevel();
      memberEntity.setLevelId(levelEntity.getId());

      //检查用户名和手机号是否唯一。感知异常，异常机制
        checkPhoneUnique(vo.getPhone());
        checkUserNameUnique(vo.getUserName());


      memberEntity.setMobile(vo.getPhone());
      memberEntity.setNickname(vo.getUserName());
      memberEntity.setUsername(vo.getUserName());

      //密码要进行加密存储
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode(vo.getPassword());
        memberEntity.setPassword(encode);

        //其他的默认信息
        memberEntity.setGender(0);
        memberEntity.setCreateTime(new Date());

        //保存
        baseMapper.insert(memberEntity);
    }

    @Override
    public void checkPhoneUnique(String phone) throws PhoneExistException{
        Integer phoneCount = baseMapper.selectCount(new QueryWrapper<MemberEntity>().eq("mobile", phone));
        if (phoneCount>0) {
            throw new PhoneExistException();
        }
    }

    @Override
    public void checkUserNameUnique(String userName) throws UserNameExistException{
        Integer userNameCount = baseMapper.selectCount(new QueryWrapper<MemberEntity>().eq("username", userName));
        if (userNameCount > 0) {
            throw new UserNameExistException();
        }
    }

    @Override
    public MemberEntity login(MemberUserLoginVo vo) {
        String loginacct = vo.getLoginacct();
        String password = vo.getPassword();

        //1、去数据库查询 SELECT * FROM ums_member WHERE username = ? OR mobile = ?
        MemberEntity entity = baseMapper.selectOne(new QueryWrapper<MemberEntity>().eq("username", loginacct).or()
                .eq("mobile", loginacct));
        if (entity==null){
            //登录失败
            return null;
        }else {
            //1.获取到数据库的password
            String passwordDb = entity.getPassword();
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            //2.密码匹配
            boolean matches = passwordEncoder.matches(password, passwordDb);
            if (matches){
                return entity;
            }
        }
        return null;
    }

    @Override
    public MemberEntity login(GiteeSocialUser socialUser) throws Exception {
        Map<String, String> query = new HashMap<>();
        query.put("access_token", socialUser.getAccessToken());
        HttpResponse response = HttpUtils.doGet("https://gitee.com", "/api/v5/user", "get", new HashMap<String, String>(), query);
        String json = EntityUtils.toString(response.getEntity());
        JSONObject jsonObject = JSON.parseObject(json);
        String id = jsonObject.getString("id");
        String name = jsonObject.getString("name");
        String gender = jsonObject.getString("gender");
        String profileImageUrl = jsonObject.getString("avatar_url");
        //具有登录和注册逻辑
        String uid = id;

        //1、判断当前社交用户是否已经登录过系统
        MemberEntity memberEntity = baseMapper.selectOne(new QueryWrapper<MemberEntity>().eq("social_uid", uid));

        if (memberEntity != null) {
            //这个用户已经注册过
            //更新用户的访问令牌的时间和access_token
            MemberEntity update = new MemberEntity();
            update.setId(memberEntity.getId());
            update.setAccessToken(socialUser.getAccessToken());
            update.setExpiresIn(socialUser.getExpiresIn());
            baseMapper.updateById(update);

            memberEntity.setAccessToken(socialUser.getAccessToken());
            memberEntity.setExpiresIn(socialUser.getExpiresIn());
            return memberEntity;
        } else {
            //2、没有查到当前社交用户对应的记录我们就需要注册一个
            MemberEntity register = new MemberEntity();
            //3、查询当前社交用户的社交账号信息（昵称、性别等）
            // 远程调用，不影响结果
            try {
//                Map<String, String> query = new HashMap<>();
//                query.put("access_token", socialUser.getAccessToken());
//                HttpResponse response = HttpUtils.doGet("https://gitee.com", "/api/v5/user", "get", new HashMap<String, String>(), query);

                if (response.getStatusLine().getStatusCode() == 200) {
                    //查询成功
//                    String gender = jsonObject.getString("gender");
                    register.setUsername(name);
                    register.setNickname(name);
                    register.setCreateTime(new Date());
                    register.setGender("m".equals(gender) ? 1 : 0);
                    register.setHeader(profileImageUrl);
                }
            }catch (Exception e){}
            register.setCreateTime(new Date());
            register.setSocialUid(uid);
            register.setAccessToken(socialUser.getAccessToken());
            register.setExpiresIn(socialUser.getExpiresIn());

            //把用户信息插入到数据库中
            baseMapper.insert(register);
            return register;
        }
    }

}