package com.kingdee.eas.basedata.master.material.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.kingdee.bos.BOSException;
import com.kingdee.bos.ctrl.swing.KDWorkButton;
import com.kingdee.eas.custom.BusinessToOAFacadeFactory;
import com.kingdee.eas.custom.IBusinessToOAFacade;
import com.kingdee.eas.util.client.MsgBox;

public class MaterialListUICTEx extends MaterialListUI {

	public MaterialListUICTEx() throws Exception {
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

	/**
	 * 
	 */
	private static final long serialVersionUID = 3250357599012703901L;

	
	
	private void sendInfoToOA() {
		String msg ="";
		if(getSelectedIdValues() !=null && getSelectedIdValues().size() >0){
			StringBuffer idsbur = new StringBuffer();
			
			for(int i =0 ; i < getSelectedIdValues().size() ; i++){
				idsbur.append(getSelectedIdValues().get(i));
				if(i<  getSelectedIdValues().size() -1)
					idsbur.append("&");
			}
			
			if(idsbur.length()>0){
				try {
	 				 IBusinessToOAFacade ibf = BusinessToOAFacadeFactory.getRemoteInstance();
					 ibf.updateMaterialInfo(idsbur.toString()); 
					msg ="ͬ��OA�ɹ�";
				} catch (BOSException e) {
					e.printStackTrace();
					msg ="ͬ��OAʧ��";
				}
				MsgBox.showInfo(msg);
			}
 		} 
	
	}
}
