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
		btnModify.setText("同步OA");// 设置按钮名称
		btnModify.setIcon(com.kingdee.eas.util.client.EASResource.getIcon("imgTbtn_edit"));// 图标
		toolBar.add(btnModify);// 添加到工具栏
		btnModify.setVisible(true);// 设置可见
		btnModify.setEnabled(true);// 设置可用
		btnModify.addActionListener(new ActionListener() {// 添加点击事件
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
					msg ="同步OA成功";
				} catch (BOSException e) {
					e.printStackTrace();
					msg ="同步OA失败";
				}
				MsgBox.showInfo(msg);
			}
	}
	
}
