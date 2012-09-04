/*
 * @(#)SynUtil.java
 * 
 * Copyright @ 2001-2012 SANY Group Co.,Ltd.
 * All right reserved.
 * 
 * 这个软件是属于三一集团有限公司机密的和私有信息，不得泄露。
 * 并且只能由三一集团有限公司内部员工在得到许可的情况下才允许使用。
 * This software is the confidential and proprietary information
 * of SANY Group Co, Ltd. You shall not disclose such
 * Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with
 * SANY Group Co, Ltd.
 */
package com.sany.masterdata.hr.sync;

import com.sany.masterdata.task.HrSyncTask;

/**
 * @author yinbp
 * @since 2012-3-27 上午9:43:02
 */
public class SynUtil {

    public static void main(String[] args) {
//        SyncJobInfo test = new SyncJobInfo();
//        test.syncAllData();
//        SyncOrganizationInfo test_ = new SyncOrganizationInfo();
//        test_.syncAllData();
//        SyncUserInfo test__ = new SyncUserInfo();
//        test__.syncAllData();
    	HrSyncTask task = new HrSyncTask();
//    	task.syncAllData();
    	task.initialData();
    }

}
