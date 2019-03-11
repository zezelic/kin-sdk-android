package kin.recovery;

import static kin.recovery.BackupManager.APP_ID_EXTRA;
import static kin.recovery.BackupManager.NETWORK_PASSPHRASE_EXTRA;
import static kin.recovery.BackupManager.NETWORK_URL_EXTRA;
import static kin.recovery.BackupManager.PUBLIC_ADDRESS_EXTRA;
import static kin.recovery.BackupManager.STORE_KEY_EXTRA;
import static kin.recovery.events.CallbackManager.REQ_CODE_BACKUP;
import static kin.recovery.events.CallbackManager.REQ_CODE_RESTORE;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import kin.recovery.backup.view.BackupActivity;
import kin.recovery.restore.view.RestoreActivity;
import kin.sdk.KinClient;

class Launcher {

	private final Activity activity;
	private final KinClient kinClient;

	Launcher(@NonNull final Activity activity, @NonNull final KinClient kinClient) {
		this.activity = activity;
		this.kinClient = kinClient;
	}

	void backupFlow(String publicAddress) {
		Intent intent = new Intent(activity, BackupActivity.class);
		addKinClientExtras(intent);
		intent.putExtra(PUBLIC_ADDRESS_EXTRA, publicAddress);
		startForResult(intent, REQ_CODE_BACKUP);
	}

	void restoreFlow() {
		Intent intent = new Intent(activity, RestoreActivity.class);
		addKinClientExtras(intent);
		startForResult(intent, REQ_CODE_RESTORE);
	}

	private void addKinClientExtras(Intent intent) {
		intent.putExtra(NETWORK_URL_EXTRA, kinClient.getEnvironment().getNetworkUrl());
		intent.putExtra(NETWORK_PASSPHRASE_EXTRA, kinClient.getEnvironment().getNetworkPassphrase());
		intent.putExtra(APP_ID_EXTRA, kinClient.getAppId());
		intent.putExtra(STORE_KEY_EXTRA, kinClient.getStoreKey());
	}

	private void startForResult(@NonNull final Intent intent, final int reqCode) {
		activity.startActivityForResult(intent, reqCode);
		activity.overridePendingTransition(R.anim.kinrecovery_slide_in_right, R.anim.kinrecovery_slide_out_left);
	}
}
