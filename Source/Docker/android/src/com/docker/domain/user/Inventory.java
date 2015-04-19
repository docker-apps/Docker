package com.docker.domain.user;

import java.util.Date;

import android.app.Activity;
import android.util.Log;
import billing.IabHelper;
import billing.IabHelper.QueryInventoryFinishedListener;
import billing.IabResult;
import billing.Purchase;

import com.docker.android.AndroidLauncher;


public class Inventory implements IInventory{
	// TODO: Only for testing purposes. Must be deleted before production.
	static final String TEST_PRODUCT_ID = "android.test.purchased";

	static final String PREMIUM_PRODUCT_ID = "premium";

	private Date lastUpdatedAt = null;

	private boolean isPremium = false;

	private Activity context;
	private IabHelper mHelper;

	public IabHelper getmHelper() {
		return mHelper;
	}

	public Inventory(Activity context, String base64EncodedPublicKey){
		this.context = context;
		Log.d(AndroidLauncher.TAG, "Creating IAB helper.");
		mHelper = new IabHelper(context, base64EncodedPublicKey);

		//TODO: Disable this for production
		mHelper.enableDebugLogging(true);

		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			public void onIabSetupFinished(IabResult result) {
				if (!result.isSuccess()) {
					// Oh noes, there was a problem.
					Log.d(AndroidLauncher.TAG, "Problem setting up In-app Billing: " + result);
				}

				// In case mHelper has been disposed in the meantime?
				if (mHelper == null) return;

				Log.d(AndroidLauncher.TAG, "Setup successful. Querying inventory.");
				update();
			}
		}); 
	}
	
	@Override
	public void update(final IInventoryCallback callback) {
		QueryInventoryFinishedListener listener = new QueryInventoryFinishedListener() {
			@Override
			public void onQueryInventoryFinished(IabResult result,
					billing.Inventory inv) {
				mGotInventoryListener.onQueryInventoryFinished(result, inv);
				callback.call();
			}
		};

		update(listener);
	}
	
	@Override
	public void update() {
		update(mGotInventoryListener);
	}
	
	private void update(final QueryInventoryFinishedListener listener){
		Log.d(AndroidLauncher.TAG, "Querying inventory.");

		context.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mHelper.queryInventoryAsync(listener);
			}
		});
	}

	// Listener that's called when we finish querying the items and subscriptions we own
	IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
		public void onQueryInventoryFinished(IabResult result, billing.Inventory inventory) {
			Log.d(AndroidLauncher.TAG, "Query inventory finished.");
			
			// Have we been disposed of in the meantime? If so, quit.
			if (mHelper == null) return;

			// Is it a failure?
			if (result.isFailure()) {
				Log.d(AndroidLauncher.TAG, "Failed to query inventory: " + result);
				return;
			}
			lastUpdatedAt = new Date();
			Log.d(AndroidLauncher.TAG, "Query inventory was successful.");

			// Do we have the premium upgrade?
			Purchase premiumPurchase = inventory.getPurchase(PREMIUM_PRODUCT_ID);
			isPremium = (premiumPurchase != null && verifyDeveloperPayload(premiumPurchase));
			Log.d(AndroidLauncher.TAG, "User is " + (isPremium ? "PREMIUM" : "NOT PREMIUM"));

			// TODO: remove this for production
			if(isPremium){
				Log.d(AndroidLauncher.TAG, "Consuming Premium for testing purposes");
				mHelper.consumeAsync(premiumPurchase, new IabHelper.OnConsumeFinishedListener() {

					@Override
					public void onConsumeFinished(Purchase purchase, IabResult result) {
						Log.d(AndroidLauncher.TAG, "isPremium consumed.");
					}
				});
				
				
			}
		}
	};

	/** Verifies the developer payload of a purchase. */
	boolean verifyDeveloperPayload(Purchase p) {
		//String payload = p.getDeveloperPayload();

		/*
		 * TODO: verify that the developer payload of the purchase is correct. It will be
		 * the same one that you sent when initiating the purchase.
		 *
		 * WARNING: Locally generating a random string when starting a purchase and
		 * verifying it here might seem like a good approach, but this will fail in the
		 * case where the user purchases an item on one device and then uses your app on
		 * a different device, because on the other device you will not have access to the
		 * random string you originally generated.
		 *
		 * So a good developer payload has these characteristics:
		 *
		 * 1. If two different users purchase an item, the payload is different between them,
		 *    so that one user's purchase can't be replayed to another user.
		 *
		 * 2. The payload must be such that you can verify it even when the app wasn't the
		 *    one who initiated the purchase flow (so that items purchased by the user on
		 *    one device work on other devices owned by the user).
		 *
		 * Using your own server to store and verify developer payloads across app
		 * installations is recommended.
		 */

		return true;
	}

	

	public Date getLastUpdateDate(){
		return lastUpdatedAt;
	}

	public boolean hasBeenUpdated(){
		return lastUpdatedAt != null;
	}

	public boolean hasPremium(){
		return isPremium;
	}

	public void buyPremium(final IInventoryCallback updateCallback){
		final IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
			@Override
			public void onIabPurchaseFinished(IabResult result, Purchase info) {
				if (result.isFailure()) {
					Log.d(AndroidLauncher.TAG, "Error purchasing: " + result);
					return;
				}

				Log.d(AndroidLauncher.TAG, "Purchase of Item " + info.getSku() + " succesful!");
				update(updateCallback);
			}
		};
		
		if (mHelper != null) mHelper.flagEndAsync();
		context.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mHelper.launchPurchaseFlow(context, PREMIUM_PRODUCT_ID, 101, mPurchaseFinishedListener, "asdf");
			}
		});
	}

	public void dispose(){
		if (mHelper != null) mHelper.dispose();
		mHelper = null;
	}
}
