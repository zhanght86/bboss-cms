

package com.frameworkset.platform.sysmgrcore.facade;

import java.io.Serializable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.frameworkset.platform.sysmgrcore.entity.Attrdesc;
import com.frameworkset.platform.sysmgrcore.entity.Group;
import com.frameworkset.platform.sysmgrcore.entity.Job;
import com.frameworkset.platform.sysmgrcore.entity.Operation;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.entity.Res;
import com.frameworkset.platform.sysmgrcore.entity.Restype;
import com.frameworkset.platform.sysmgrcore.entity.Role;
import com.frameworkset.platform.sysmgrcore.entity.User;

/**
 * 项目：SysMgrCore <br>
 * 描述：系统持久化层访问门户类，该门户为应用程序提供了尽可能丰富的接口，但这些接口 将视应用程序的使用情况来决定是否实现其具体的功能。 <br>
 * 版本：1.0 <br>
 * 
 * @author 吴卫雄
 */
public class PersistentFacade implements Serializable {
    private static Logger logger = LoggerFactory.getLogger(PersistentFacade.class
            .getName());

    /**
     * 存储用户实例
     * 
     * @param roles
     * @param user
     * @return
     * @roseuid 43E9A92F0196
     */
    public boolean storeUser(User user) {
        return false;
    }

    /**
     * 删除用户实例同时将连带删除与该实例有关的所有实例，如：用户与角色关系实例。
     * 
     * @param user
     * @return
     * @roseuid 43E9A92F0203
     */
    public boolean deleteUser(User user) {
        return false;
    }

    /**
     * 根据机构删除该机构与用户的关系
     * 
     * @param org
     * @return boolean
     * @roseuid 43EAA92E02EE
     */
    public boolean deleteUserAndOrg(Organization org) {
        return true;
    }

    /**
     * 根据岗位删除该岗位与用户的关系。
     * 
     * @param job
     * @return Boolean
     * @roseuid 43EAA99300AB
     */
    public boolean deleteUserAndJob(Job job) {
        return true;
    }

    /**
     * 根据组删除该组与用户的关系
     * 
     * @param group
     * @return boolean
     * @roseuid 43EAB58C001F
     */
    public boolean deleteUserAndGroup(Group group) {
        return true;
    }

    /**
     * 根据角色删除该角色与用户的关系
     * 
     * @param role
     * @return Boolean
     * @roseuid 43EADADC0232
     */
    public boolean deleteUserAndRole(Role role) {
        return true;
    }

    /**
     * 根据用户登录名提取指定用户
     * 
     * @param userName
     * @return com.frameworkset.platform.sysmgrcore.entity.User
     * @roseuid 43E9AADC006D
     */
    public User getUserByName(String userName) {
        return null;
    }

    /**
     * 根据角色取所有相关的用户
     * 
     * @param role
     * @return TList
     * @roseuid 43E9ABFF0167
     */
    public List getUserByRole(Role role) {
        return null;
    }

    /**
     * 根据岗位取所有的用户
     * 
     * @param job
     * @return TList
     * @roseuid 43E9ADA6031C
     */
    public List getUserByJob(Job job) {
        return null;
    }

    /**
     * 根据机构取所有的用户
     * 
     * @param org
     * @return TList
     * @roseuid 43E9B26200DA
     */
    public List getUserByOrg(Organization org) {
        return null;
    }

    /**
     * 根据组织取所有的用户
     * 
     * @param group
     * @return TList
     * @roseuid 43E9B614001F
     */
    public List getUserByGroup(Group group) {
        return null;
    }

    /**
     * 根据操作取相关用户
     * 
     * @param oper
     * @return com.frameworkset.platform.sysmgrcore.entity.User
     * @roseuid 43EABA94029F
     */
    public User getUserByOper(Operation oper) {
        return null;
    }

    /**
     * 取所有用户
     * 
     * @return TList
     * @roseuid 43E9AB2A032C
     */
    public List getUserList() {
        return null;
    }

    /**
     * 存储角色实例
     * 
     * @param role
     * @return
     * @roseuid 43E9A92F0232
     */
    public boolean storeRole(Role role) {
        return false;
    }

    /**
     * 根据角色名称取角色实例
     * 
     * @param roleName
     * @return com.frameworkset.platform.sysmgrcore.entity.Role
     * @roseuid 43E9AB780232
     */
    public Role getRoleByName(String roleName) {
        return null;
    }

    /**
     * 根据用户取所有相关的角色。
     * 
     * @param user
     * @return TList
     * @roseuid 43E9AEDD031C
     */
    public List getRoleByUser(User user) {
        return null;
    }

    /**
     * 根据资源取所有相关的角色。
     * 
     * @param res
     * @return List
     * @roseuid 43E9B03E0261
     */
    public List getRoleByRes(Res res) {
        return null;
    }

    /**
     * 根据操作取所有的角色
     * 
     * @param oper
     * @return List
     * @roseuid 43E9B10D002E
     */
    public List getRoleByOper(Operation oper) {
        return null;
    }

    /**
     * 取所有角色
     * 
     * @return List
     * @roseuid 43E9AC6A003E
     */
    public List getRoleList() {
        return null;
    }

    /**
     * 删除角色实例同时将连带删除与该实例有关的所有实例，如：用户与角色关系实例。
     * 
     * @param role
     * @return
     * @roseuid 43E9A92F0251
     */
    public boolean deleteRole(Role role) {
        return false;
    }

    /**
     * 根据用户删除该用户与角色的关系
     * 
     * @param user
     * @return Boolean
     * @roseuid 43EAB0EC002E
     */
    public Boolean deleteRoleAndUser(User user) {
        return null;
    }

    /**
     * 存储岗位
     * 
     * @param job
     * @return boolean
     * @roseuid 43E9ACCF0271
     */
    public boolean storeJob(Job job) {
        return true;
    }

    /**
     * 根据名称取岗位
     * 
     * @param jobName
     * @return List
     * @roseuid 43E9AD100148
     */
    public List getJobByName(String jobName) {
        return null;
    }

    /**
     * 根据用户取相关岗位
     * 
     * @param user
     * @return List
     * @roseuid 43E9AE06029F
     */
    public List getJobByUser(User user) {
        return null;
    }

    /**
     * 根据机构取相关岗位
     * 
     * @param org
     * @return List
     * @roseuid 43E9AE5F0119
     */
    public List getJobByOrg(Organization org) {
        return null;
    }

    /**
     * 删除岗位
     * 
     * @param job
     * @return boolen
     * @roseuid 43E9AD5B02CE
     */
    public boolean deleteJob(Job job) {
        return true;
    }

    /**
     * 根据机构删除该机构与用户的关系
     * 
     * @param org
     * @return Boolean
     * @roseuid 43EAB4A5029F
     */
    public boolean deleteJobAndOrg(Organization org) {
        return true;
    }

    /**
     * 存储机构
     * 
     * @param org
     * @return boolean
     * @roseuid 43E9B3EA0167
     */
    public boolean storeOrg(Organization org) {
        return true;
    }

    /**
     * 根据名称取机构
     * 
     * @param orgName
     * @return com.frameworkset.platform.sysmgrcore.entity.Organization
     * @roseuid 43E9B3F701E4
     */
    public Organization getOrgByName(String orgName) {
        return null;
    }

    /**
     * 根据用户取机构
     * 
     * @param user
     * @return List
     * @roseuid 43E9B41B030D
     */
    public List getOrgByUser(User user) {
        return null;
    }

    /**
     * 根据岗位取机构
     * 
     * @param job
     * @return List
     * @roseuid 43E9B42F0148
     */
    public List getOrgByJob(Job job) {
        return null;
    }

    /**
     * 根据组取机构
     * 
     * @param group
     * @return List
     * @roseuid 43E9B5DD00BB
     */
    public List getOrgByGroup(Group group) {
        return null;
    }

    /**
     * 取所有机构
     * 
     * @return com.frameworkset.platform.sysmgrcore.entity.Organization
     * @roseuid 43E9B56201E4
     */
    public Organization getOrgList() {
        return null;
    }

    /**
     * 删除机构
     * 
     * @param org
     * @return boolean
     * @roseuid 43E9B89C008C
     */
    public boolean deleteOrg(Organization org) {
        return true;
    }

    /**
     * 存储资源
     * 
     * @param res
     * @return boolean
     * @roseuid 43E9B96B00FA
     */
    public boolean storeRes(Res res) {
        return true;
    }

    /**
     * 取所有资源
     * 
     * @return com.frameworkset.platform.sysmgrcore.entity.Res
     * @roseuid 43E9B974033C
     */
    public Res getResList() {
        return null;
    }

    /**
     * 根据操作取资源
     * 
     * @param oper
     * @return List
     * @roseuid 43E9B9AF035B
     */
    public List getResByOper(Operation oper) {
        return null;
    }

    /**
     * 根据用户取资源
     * 
     * @param user
     * @return List
     * @roseuid 43E9BA6F008C
     */
    public List getResByUser(User user) {
        return null;
    }

    /**
     * 根据角色取资源
     * 
     * @param role
     * @return List
     * @roseuid 43E9BB1A00FA
     */
    public List getResByRole(Role role) {
        return null;
    }

    /**
     * 删除资源
     * 
     * @param res
     * @return boolean
     * @roseuid 43E9BB4F0167
     */
    public boolean deleteRes(Res res) {
        return true;
    }

    /**
     * 存储资源类型
     * 
     * @param resType
     * @return boolean
     * @roseuid 43EADE1A0167
     */
    public boolean storeResType(Restype resType) {
        return true;
    }

    /**
     * 根据名称取资源类型
     * 
     * @param resTypeName
     * @return com.frameworkset.platform.sysmgrcore.entity.Restype
     * @roseuid 43EADE2C038A
     */
    public Restype getResTypeByName(String resTypeName) {
        return null;
    }

    /**
     * 根据资源取资源类型
     * 
     * @param res
     * @return com.frameworkset.platform.sysmgrcore.entity.Restype
     * @roseuid 43EADE8400BB
     */
    public Restype getResTypeByRes(Res res) {
        return null;
    }

    /**
     * 根据属性说明取资源类型
     * 
     * @param attrdesc
     * @return com.frameworkset.platform.sysmgrcore.entity.Restype
     * @roseuid 43EADE9E0128
     */
    public Restype getResTypeByAttrDesc(Attrdesc attrdesc) {
        return null;
    }

    /**
     * 取所有资源类型
     * 
     * @return com.frameworkset.platform.sysmgrcore.entity.Restype
     * @roseuid 43EADE94032C
     */
    public Restype getResTypeList() {
        return null;
    }

    /**
     * 删除资源类型。当前删除支持级联删除。
     * 
     * @param resType
     * @return boolean
     * @roseuid 43EADED400DA
     */
    public boolean deleteResType(Restype resType) {
        return true;
    }

    /**
     * 存储属性描述
     * 
     * @param attrdesc
     * @return boolean
     * @roseuid 43EAE17C029F
     */
    public boolean storeAttrdesc(Attrdesc attrdesc) {
        return true;
    }

    /**
     * 取所有属性描述
     * 
     * @return com.frameworkset.platform.sysmgrcore.entity.Attrdesc
     * @roseuid 43EAE1A40222
     */
    public Attrdesc getAttrdescList() {
        return null;
    }

    /**
     * 删除指定的属性描述
     * 
     * @param attrdesc
     * @return boolean
     * @roseuid 43EAE2170242
     */
    public boolean deleteAttrdesc(Attrdesc attrdesc) {
        return true;
    }

    /**
     * 存储组
     * 
     * @param group
     * @return boolean
     * @roseuid 43EA8AA200CB
     */
    public boolean storeGroup(Group group) {
        return true;
    }

    /**
     * 根据名称取组实例
     * 
     * @param groupName
     * @return com.frameworkset.platform.sysmgrcore.entity.Group
     * @roseuid 43EA8AAA0119
     */
    public Group getGroupByName(String groupName) {
        return null;
    }

    /**
     * 根据用户取组实例
     * 
     * @param user
     * @return List
     * @roseuid 43EA8AFB0119
     */
    public List getGroupByUser(User user) {
        return null;
    }

    /**
     * 根据机构取组实例
     * 
     * @param org
     * @return List
     * @roseuid 43EA8C3F031C
     */
    public List getGroupByOrg(Organization org) {
        return null;
    }

    /**
     * 取所有组实例
     * 
     * @return List
     * @roseuid 43EA8C2502BF
     */
    public List getGroupList() {
        return null;
    }

    /**
     * 删除组
     * 
     * @param group
     * @return boolean
     * @roseuid 43EA8CC101E4
     */
    public boolean deleteGroup(Group group) {
        return true;
    }

    /**
     * 存储操作
     * 
     * @param oper
     * @return boolean
     * @roseuid 43EAD86D0177
     */
    public boolean storeOper(Operation oper) {
        return true;
    }

    /**
     * 根据资源取操作
     * 
     * @param res
     * @return boolean
     * @roseuid 43EAD8C902AF
     */
    public boolean getOperByRes(Res res) {
        return true;
    }

    /**
     * 取操作列表
     * 
     * @return com.frameworkset.platform.sysmgrcore.entity.Operation
     * @roseuid 43EADA2F007D
     */
    public Operation getOperList() {
        return null;
    }

    /**
     * 删除操作
     * 
     * @param oper
     * @return boolean
     * @roseuid 43EADA8202EE
     */
    public boolean deleteOper(Operation oper) {
        return true;
    }
}