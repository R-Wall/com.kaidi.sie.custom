package com.leoch.sie.custom.intercept;

import com.smile.interceptor.api.IWfHandler;
import com.smile.interceptor.api.WfNodeInfo;
import com.teamcenter.rac.kernel.TCCRDecision;
import com.teamcenter.rac.kernel.TCComponentTask;

/**
 * @author wall
 *
 */
public class ChangeManager implements IWfHandler {

	private final String PRG_VERISION = "V1.0";
	private WfNodeInfo nodeInfo = null;
	
	public ChangeManager() {
		initNodeInfo();
	}
	
	private void initNodeInfo() {
		
		nodeInfo = new WfNodeInfo();
		
		//��ǰ����汾
		nodeInfo.prgVersion = PRG_VERISION;
		
		//�Ի������
		nodeInfo.dlgTitle = "���";
		
		//ͬ��ѡ���ϵı�ǩ����
		nodeInfo.aprovalPrompt = "��׼";
		
		nodeInfo.rejectPrompt = "�ܾ�";
		
		//��������������ѡ��
		nodeInfo.enableNoDicession = false;
		
		//��ע�Ϳ����������ʾ��Ϣ
		nodeInfo.customizePrompt = "ѡ��״̬";
	}
	
	public WfNodeInfo getNodeInfo() {
		
		return nodeInfo;
	}



	public String handleTask(TCComponentTask task, TCCRDecision decision) {	
		String msg = Handlers.changeStatus(task, decision);

		return msg;
	}

}
