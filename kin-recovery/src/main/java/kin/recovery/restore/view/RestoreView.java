package kin.recovery.restore.view;


import kin.recovery.base.BaseView;

public interface RestoreView extends BaseView {

	void navigateToUpload();

	void navigateToEnterPassword(String keystoreData);

	void navigateToRestoreCompleted();

	void navigateBack();

	void close();

	void closeKeyboard();

	void showError();
}
