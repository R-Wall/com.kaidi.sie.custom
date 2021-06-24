package com.leoch.sie.custom.utils;

import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCSession;

public class PreferenceUtils {

	/**
	 * @Title: getPreferenceValue
	 * @Description: TODO(��ȡ��ѡ��ĵ���ֵ)
	 * @param @param session
	 * @param @param PreferenceName
	 * @param @return ����
	 * @return String ��������
	 * @throws
	 */

	public static String getPreferenceValue(TCSession session, String preferenceName) {
		return session.getPreferenceService().getStringValue(preferenceName);
	}

	/**
	 * @Title: getPreferenceValues
	 * @Description: TODO(��ȡ��ѡ����ֵ)
	 * @param @param session
	 * @param @param PreferenceName
	 * @param @return ����
	 * @return String[] ��������
	 * @throws
	 */

	public static String[] getPreferenceValues(TCSession session, String preferenceName) {
		return session.getPreferenceService().getStringValues(preferenceName);
	}

	/**
	 * @Title: getPreferenceDescription
	 * @Description: TODO(��ȡ��ѡ�������)
	 * @param @param session
	 * @param @param PreferenceName
	 * @param @return ����
	 * @return String ��������
	 * @throws
	 */

	public static String getPreferenceDescription(TCSession session, String preferenceName) {
		return session.getPreferenceService().getPreferenceDescription(preferenceName);
	}

	/**
	 * @Title: getTCComponent
	 * @Description: TODO(ͨ�������UID��ȡ����)
	 * @param @param session
	 * @param @param uid
	 * @param @return
	 * @param @throws TCException ����
	 * @return TCComponent ��������
	 * @throws
	 */

	public static TCComponent getTCComponent(TCSession session, String uid) throws TCException {
		return session.getComponentManager().getTCComponent(uid);
	}

	/**
	 * @Title: getTCComponent
	 * @Description: TODO(ͨ����ѡ�����õĶ���UID��ȥ����)
	 * @param @param PreferenceName
	 * @param @param session
	 * @param @return
	 * @param @throws TCException ����
	 * @return TCComponent ��������
	 * @throws
	 */

	public static TCComponent getTCComponent(String preferenceName, TCSession session) throws TCException {
		String uid = getPreferenceValue(session, preferenceName);
		return session.getComponentManager().getTCComponent(uid);
	}

}
