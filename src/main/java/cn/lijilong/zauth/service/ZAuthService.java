package cn.lijilong.zauth.service;


public interface ZAuthService extends GeneralService {

    boolean haveAuth(String[] values, ZAuthCallBack callBack);

    boolean haveRole(String[] values, ZAuthCallBack callBack);

    boolean haveGroup(String[] values, ZAuthCallBack callBack);

    boolean haveUser(String[] values, ZAuthCallBack callBack);

    boolean excludeAuth(String[] values, ZAuthCallBack callBack);

    boolean excludeRole(String[] values, ZAuthCallBack callBack);

    boolean excludeGroup(String[] values, ZAuthCallBack callBack);

    boolean excludeUser(String[] values, ZAuthCallBack callBack);

    boolean haveToken(ZAuthCallBack callBack);

    boolean sign(ZAuthCallBack callBack);
}
