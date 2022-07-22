/**
 * output package name
 */
package com.kingdee.eas.custom.client;


import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Map; 
import org.apache.log4j.Logger; 
import com.kingdee.bos.BOSException;
import com.kingdee.bos.ctrl.swing.KDSpinner;
import com.kingdee.bos.ui.face.CoreUIObject;
import com.kingdee.eas.custom.GLRptCompAccountBalanceFacadeFactory;
import com.kingdee.eas.fi.gl.rpt.CompanyDisplayModeEnum;
import com.kingdee.eas.fi.gl.rpt.GLRptAccountBalanceCondition;
import com.kingdee.eas.fi.gl.rpt.GLRptAccountBalanceFacadeFactory;
import com.kingdee.eas.fi.gl.rpt.GLRptBaseCondition;
import com.kingdee.eas.fi.gl.rpt.IGLRptBaseFacade;
/**
 * output class name
 */
public class GLRptCompAccountBalanceQueryUI extends AbstractGLRptCompAccountBalanceQueryUI
{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 755279275138308294L;
	private static final Logger logger = CoreUIObject.getLogger(GLRptCompAccountBalanceQueryUI.class);
    
    public GLRptCompAccountBalanceQueryUI()
    throws Exception
  {}
  
  protected void initListener()
  {
    super.initListener();
    this.chkDisplayAsstDetail.addItemListener(new ItemListener()
    {
      public void itemStateChanged(ItemEvent e)
      {
        boolean selected = e.getStateChange() == 1;
        if (!selected) {
          GLRptCompAccountBalanceQueryUI.this.chkisDCDispatchAsst.setSelected(false);
        }
        GLRptCompAccountBalanceQueryUI.this.chkisDCDispatchAsst.setEnabled(selected);
      }
    });
    this.chkShowLeafAccount.addItemListener(new ItemListener()
    {
      public void itemStateChanged(ItemEvent e)
      {
        GLRptCompAccountBalanceQueryUI.this.spnAccountLevel.setEnabled(e.getStateChange() != 1);
      }
    });
    this.chkOpAmountZero.addItemListener(new ItemListener()
    {
      public void itemStateChanged(ItemEvent e)
      {
        boolean selected = e.getStateChange() == 1;
        if (selected)
        {
          GLRptCompAccountBalanceQueryUI.this.chkOpYearAmountZero.setSelected(false);
          GLRptCompAccountBalanceQueryUI.this.chkYearAmountAndBalZero.setSelected(false);
        }
      }
    });
    this.chkOpBalanceZero.addItemListener(new ItemListener()
    {
      public void itemStateChanged(ItemEvent e)
      {
        boolean selected = e.getStateChange() == 1;
        if (selected)
        {
          GLRptCompAccountBalanceQueryUI.this.chkOpYearAmountZero.setSelected(false);
          GLRptCompAccountBalanceQueryUI.this.chkYearAmountAndBalZero.setSelected(false);
        }
      }
    });
    this.chkAmountAndBalZero.addItemListener(new ItemListener()
    {
      public void itemStateChanged(ItemEvent e)
      {
        boolean selected = e.getStateChange() == 1;
        if (selected)
        {
          GLRptCompAccountBalanceQueryUI.this.chkOpYearAmountZero.setSelected(false);
          GLRptCompAccountBalanceQueryUI.this.chkYearAmountAndBalZero.setSelected(false);
        }
      }
    });
    this.chkOpYearAmountZero.addItemListener(new ItemListener()
    {
      public void itemStateChanged(ItemEvent e)
      {
        boolean selected = e.getStateChange() == 1;
        if (selected)
        {
          GLRptCompAccountBalanceQueryUI.this.chkOpAmountZero.setSelected(false);
          GLRptCompAccountBalanceQueryUI.this.chkOpBalanceZero.setSelected(false);
          GLRptCompAccountBalanceQueryUI.this.chkAmountAndBalZero.setSelected(false);
          GLRptCompAccountBalanceQueryUI.this.chkYearAmountAndBalZero.setSelected(false);
        }
      }
    });
    this.chkYearAmountAndBalZero.addItemListener(new ItemListener()
    {
      public void itemStateChanged(ItemEvent e)
      {
        boolean selected = e.getStateChange() == 1;
        if (selected)
        {
          GLRptCompAccountBalanceQueryUI.this.chkOpAmountZero.setSelected(false);
          GLRptCompAccountBalanceQueryUI.this.chkOpBalanceZero.setSelected(false);
          GLRptCompAccountBalanceQueryUI.this.chkAmountAndBalZero.setSelected(false);
          GLRptCompAccountBalanceQueryUI.this.chkOpYearAmountZero.setSelected(false);
        }
      }
    });
  }
  
  public void setCustomCondition(GLRptBaseCondition condition)
  {
   // super.setCustomCondition(condition);
    GLRptAccountBalanceCondition cond = (GLRptAccountBalanceCondition)condition;
    if (cond == null) {
      return;
    }
    this.spnPeriodYearBegin.setValue(new Integer(condition.getPeriodYearStart()));
    this.spnPeriodNumberBegin.setValue(new Integer(condition.getPeriodNumberStart()));
    this.chkDisplayAsstDetail.setSelected(cond.getDisplayAsstDetail());
    this.chkIncludeBW.setSelected(cond.getIncludeBWAccount());
    this.chkOpYearAmountZero.setSelected(cond.getOptionYearAmountZero());
    this.chkYearAmountAndBalZero.setSelected(cond.getOptionYearAmountAndBalZero());
    this.chkisDCDispatchAsst.setSelected(cond.getOptionDCDispatchAsst());
    this.displayLeafCompany.setSelected(cond.isDisplayLeafCompany());
  }
  
  public GLRptBaseCondition getCustomCondition()
  {
    GLRptAccountBalanceCondition cond = (GLRptAccountBalanceCondition)super.getCustomCondition();
    cond.setIncludeBWAccount(this.chkIncludeBW.isSelected());
    cond.setDisplayAsstDetail(this.chkDisplayAsstDetail.isSelected());
    cond.setOptionYearAmountAndBalZero(this.chkYearAmountAndBalZero.isSelected());
    cond.setOptionYearAmountZero(this.chkOpYearAmountZero.isSelected());
    cond.setOptionDCDispatchAsst(this.chkisDCDispatchAsst.isSelected());
    cond.setDisplayLeafCompany(this.displayLeafCompany.isSelected());
    return cond;
  }
  
  public Component getFocusComponent()
  {
    return ((KDSpinner.DefaultNumberEditor)this.spnPeriodYearBegin.getEditor()).getTextField();
  }
  
  public void clear()
  {
    super.clear();
    this.chkIncludeBW.setSelected(false);
    this.chkOpYearAmountZero.setSelected(false);
    this.chkisDCDispatchAsst.setSelected(false);
    this.chkisDCDispatchAsst.setEnabled(false);
    this.chkYearAmountAndBalZero.setSelected(false);
    this.chkDisplayAsstDetail.setSelected(false);
    this.displayLeafCompany.setSelected(false);
  }
  
  @Override
  protected IGLRptBaseFacade getIGLRptBaseFacade()throws BOSException
  {
    //return GLRptAccountBalanceFacadeFactory.getRemoteInstance();
	  return GLRptCompAccountBalanceFacadeFactory.getRemoteInstance();
  }
  @Override
  protected GLRptBaseCondition createReportCondition()
  {
    return new GLRptAccountBalanceCondition();
  }
  @Override
  protected GLRptBaseCondition createReportCondition(Map param)
    throws CloneNotSupportedException
  {
    return new GLRptAccountBalanceCondition(param);
  }
  @Override
  protected boolean canMergeDisplay()
  {
    return true;
  }
  @Override
  protected boolean canDetailDisplay()
  {
    return true;
  }
  @Override
  protected void displayModeChanged()
  {
    super.displayModeChanged();
    if ((this.cbDisplayMode.getSelectedItem() == CompanyDisplayModeEnum.merger) || (this.cbDisplayMode.getSelectedItem() == CompanyDisplayModeEnum.details)) {
      this.displayLeafCompany.setVisible(true);
    } else {
      this.displayLeafCompany.setVisible(false);
    }
  }
  
 
}