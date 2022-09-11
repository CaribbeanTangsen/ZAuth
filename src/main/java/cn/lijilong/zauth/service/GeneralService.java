package cn.lijilong.zauth.service;

import java.util.Collection;

public interface GeneralService {

    Collection<String> getAllAuth(Long uid);

    Collection<String> getAllRole(Long uid);

    Collection<String> getAllGroup(Long uid);

}