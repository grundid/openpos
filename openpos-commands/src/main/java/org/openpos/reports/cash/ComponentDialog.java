package org.openpos.reports.cash;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JFrame;

public class ComponentDialog extends javax.swing.JDialog {

	private boolean okPressed = false;

	/** Creates new form JMessageDialog */
	private ComponentDialog(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
	}

	private ComponentDialog(java.awt.Dialog parent, boolean modal) {
		super(parent, modal);
	}

	private static Window getWindow(Component parent) {
		if (parent == null) {
			return new JFrame();
		}
		else if (parent instanceof Frame || parent instanceof Dialog) {
			return (Window)parent;
		}
		else {
			return getWindow(parent.getParent());
		}
	}

	public static boolean showMessage(Component parent, CashFromToView content) {
		Window window = getWindow(parent);
		ComponentDialog myMsg;
		if (window instanceof Frame) {
			myMsg = new ComponentDialog((Frame)window, true);
		}
		else {
			myMsg = new ComponentDialog((Dialog)window, true);
		}
		myMsg.initComponents(content);
		myMsg.applyComponentOrientation(parent.getComponentOrientation());
		myMsg.getRootPane().setDefaultButton(myMsg.jcmdOK);
		//myMsg.show();
		myMsg.setVisible(true);
		return myMsg.okPressed;
	}

	private void initComponents(final CashFromToView content) {
		jPanel3 = new javax.swing.JPanel();
		jcmdOK = new javax.swing.JButton();
		jcmdCancel = new JButton();
		JButton buttonVormittag = new JButton();
		buttonVormittag.setText("8:00-14:00");
		buttonVormittag.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				content.setFromToTime(8 * 60, 14 * 60);
			}
		});
		JButton buttonNachmittag = new JButton();
		buttonNachmittag.setText("14:00-18:00");
		buttonNachmittag.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				content.setFromToTime(14 * 60, 18 * 60);
			}
		});
		setTitle("Zwischenbericht"); // NOI18N
		setResizable(true);
		setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));
		addWindowListener(new java.awt.event.WindowAdapter() {

			@Override
			public void windowClosing(java.awt.event.WindowEvent evt) {
				closeDialog();
			}
		});
		jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.X_AXIS));
		//		jPanel3.add(content);
		jcmdOK.setText("   OK   ");
		jcmdOK.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jcmdOKActionPerformed(evt);
			}
		});
		jcmdCancel.setText("Abbrechen"); // NOI18N
		jcmdCancel.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jcmdMoreActionPerformed(evt);
			}
		});
		jPanel3.add(buttonVormittag);
		jPanel3.add(buttonNachmittag);
		jPanel3.add(jcmdOK);
		jPanel3.add(jcmdCancel);
		getContentPane().add(content);
		getContentPane().add(jPanel3);
		java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		int width = 700;
		int height = 450;
		setBounds((screenSize.width - width) / 2, (screenSize.height - height) / 2, width, height);
	}

	private void jcmdMoreActionPerformed(java.awt.event.ActionEvent evt) {
		closeDialog();
	}

	private void jcmdOKActionPerformed(java.awt.event.ActionEvent evt) {
		okPressed = true;
		closeDialog();
	}

	private void closeDialog() {
		setVisible(false);
		dispose();
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JPanel jPanel3;
	private javax.swing.JButton jcmdOK;
	private javax.swing.JButton jcmdCancel;
	// End of variables declaration//GEN-END:variables
}
