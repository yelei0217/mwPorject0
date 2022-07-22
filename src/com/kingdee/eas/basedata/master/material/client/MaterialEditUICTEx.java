package com.kingdee.eas.basedata.master.material.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.kingdee.bos.BOSException;
import com.kingdee.bos.ctrl.swing.KDWorkButton;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.eas.custom.BusinessFormHisFactory;
import com.kingdee.eas.custom.BusinessToOAFacadeFactory;
import com.kingdee.eas.custom.IBusinessFormHis;
import com.kingdee.eas.custom.IBusinessToOAFacade;
import com.kingdee.eas.util.client.MsgBox;

public class MaterialEditUICTEx extends MaterialEditUI {

	private static final long serialVersionUID = 4402325932054709231L;
	public MaterialEditUICTEx() throws Exception {
		super();

		KDWorkButton btnModify = new KDWorkButton();
		btnModify.setText("ͬ��OA");// ���ð�ť����
		btnModify.setIcon(com.kingdee.eas.util.client.EASResource.getIcon("imgTbtn_edit"));// ͼ��
		toolBar.add(btnModify);// ��ӵ�������
		btnModify.setVisible(true);// ���ÿɼ�
		btnModify.setEnabled(true);// ���ÿ���
		btnModify.addActionListener(new ActionListener() {// ��ӵ���¼�
			@Override
			public void actionPerformed(ActionEvent e) {
				sendInfoToOA();
			}
		});
	}
	private void sendInfoToOA() {
		String msg ="";
			if(editData.getId() != null && !"".equals(editData.getId().toString())){
				try {
					 String id = editData.getId().toString();
					 IBusinessToOAFacade ibf = BusinessToOAFacadeFactory.getRemoteInstance();
					 ibf.updateMaterialInfo(id); 
					msg ="ͬ��OA�ɹ�";
				} catch (BOSException e) {
					e.printStackTrace();
					msg ="ͬ��OAʧ��";
				}
				MsgBox.showInfo(msg);
			}
	}
	
}
