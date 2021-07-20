package com.ec.custom.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCAttachmentScope;
import com.teamcenter.rac.kernel.TCAttachmentType;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentItem;
import com.teamcenter.rac.kernel.TCComponentTask;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.util.MessageBox;

import cocom.leoch.sie.custom.oa.action.GetOADocAction;

public class GetECChangeHandlers extends AbstractHandler {

	TCSession session;
	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
				
		TCComponent tcc = (TCComponent) AIFUtility.getCurrentApplication().getTargetComponent();
		if (!(tcc instanceof TCComponentTask)) {
			MessageBox.post("��ѡ������������в�����", "��ʾ", MessageBox.INFORMATION);
			return null;
		}
		TCComponentTask task = (TCComponentTask) tcc;
		TCComponent[] targets = null;		
		try {
			targets = task.getRoot().getAttachments(TCAttachmentScope.LOCAL, TCAttachmentType.TARGET);
		} catch (TCException e1) {
			e1.printStackTrace();
		}
		TCComponentItem ecn = null;		
		for (int i = 0; i < targets.length; i++) {
			TCComponent target = targets[i];
			String type = target.getType();
			if (!"K8_EC".equals(type)) {
				continue;
			}
			ecn = (TCComponentItem) target;
			break;
		}
		if(ecn==null){
			MessageBox.post("����Ŀ����û��EC�����޷�����EC��OA���̵��ţ���ȡ����", "��ʾ", MessageBox.INFORMATION);
			return null;
		}
		try {
			String oaid = null;
			oaid = ecn.getProperty("k8_OA");
			if(oaid.equals("")){
				MessageBox.post("ECû�����̵��ţ��޷���ȡOA�Ĺ鵵������������дOA���̵���", "��ʾ", MessageBox.INFORMATION);
			}
			GetOADocAction action = new GetOADocAction();
			String msg = action.sent(oaid,ecn);
			MessageBox.post(msg, "��ʾ", MessageBox.INFORMATION);
		} catch (Exception e) {
			MessageBox.post(e.toString(), "����", MessageBox.ERROR);
			e.printStackTrace();
		}		
		return null;
	}

}
