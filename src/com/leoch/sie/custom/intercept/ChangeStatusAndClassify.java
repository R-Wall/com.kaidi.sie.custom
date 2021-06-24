package com.leoch.sie.custom.intercept;

import com.smile.interceptor.api.IWfHandler;
import com.smile.interceptor.api.WfNodeInfo;
import com.teamcenter.rac.kernel.TCCRDecision;
import com.teamcenter.rac.kernel.TCComponentTask;

public class ChangeStatusAndClassify implements IWfHandler {

	
	private final String PRG_VERISION = "V1.0";
	private WfNodeInfo nodeInfo = null;
	
	public ChangeStatusAndClassify() {
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
		nodeInfo.customizePrompt = "ѡ��״̬���鵵";
	}
	
	public WfNodeInfo getNodeInfo() {
		
		return nodeInfo;
	}

	@Override
	public String handleTask(TCComponentTask task, TCCRDecision decision) {
		String msg = Handlers.changeStatus(task, decision);
		if (msg != null) {
			return msg;
		}
		msg = Handlers.classifyAndSentMail(task, decision);
		return msg;
	}
}
