package com.leoch.sie.custom.sap.handlers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.leoch.sie.custom.sap.action.BOMSentToSAPAction;
import com.leoch.sie.custom.utils.Demo;
import com.leoch.sie.custom.utils.RuleCheck;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCAttachmentScope;
import com.teamcenter.rac.kernel.TCAttachmentType;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCComponentTask;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.util.MessageBox;

public class BOMSentToSapHandler extends AbstractHandler{
	
	public Demo demo;

	@Override
	public Object execute(ExecutionEvent e) throws ExecutionException {
		TCComponent tcc = (TCComponent) AIFUtility.getCurrentApplication().getTargetComponent();
		if (!(tcc instanceof TCComponentTask)) {
			MessageBox.post("��ѡ������������в�����", "��ʾ", MessageBox.INFORMATION);
			return null;
		}

		TCComponentTask task = (TCComponentTask) tcc;
		try {
			boolean checked = RuleCheck.check("BOM", task);
			if (!checked) {
				MessageBox.post("��ǰ����������BOM������SAP����", "��ʾ", MessageBox.INFORMATION);
				return null;
			}
			final List<TCComponentItemRevision> revs = new ArrayList<>();
			TCComponent[] targets = task.getRoot().getAttachments(TCAttachmentScope.LOCAL, TCAttachmentType.TARGET);
			for (int i = 0; i < targets.length; i++) {
				TCComponent target = targets[i];
				if (target instanceof TCComponentItemRevision) {
					TCComponentItemRevision part = (TCComponentItemRevision) target;
					String part_type = part.getType();
					if (part_type.endsWith("PartRevision")) {
						revs.add(part);
					}
				}
			}
			if (revs == null || revs.size() == 0) {
				MessageBox.post("����Ŀ����û�����ϣ�", "��ʾ", MessageBox.INFORMATION);
				return null;
			}
			demo = new Demo();
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					BOMSentToSAPAction action = new BOMSentToSAPAction(revs);
					demo.start();
					action.excute();
					demo.close();
					
				}
			}).start();
			
		} catch (TCException exp) {
			MessageBox.post(exp);
			exp.printStackTrace();
		}
		
		return null;
	}

}
