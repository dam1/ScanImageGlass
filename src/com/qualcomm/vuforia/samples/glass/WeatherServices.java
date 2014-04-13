package com.qualcomm.vuforia.samples.glass;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.TimelineManager;
import com.qualcomm.vuforia.samples.Books.R;


// ...
public class WeatherServices extends Service
{
  // "Life cycle" constants

  // [1] Starts from this..
  private static final int STATE_NORMAL = 1;

  // [2] When panic action has been triggered by the user.
  private static final int STATE_PANIC_TRIGGERED = 2;

  // [3] Note that cancel, or successful send, etc. change the state back to normal
  // These are intermediate states...
  private static final int STATE_CANCEL_REQUESTED = 4;
  private static final int STATE_CANCEL_PROCESSED = 8;
  private static final int STATE_PANIC_PROCESSED = 16;

  private String textGlass;
  // ....

  // Global "state" of the service.
  // Currently not being used...
  private int currentState;


  // For live card
  private LiveCard liveCard;

//  public WeatherObject weatherObject;


  // No need for IPC...
  public class LocalBinder extends Binder {
    public WeatherServices getService() {
      return WeatherServices.this;
    }
  }
  private final IBinder mBinder = new LocalBinder();


  public WeatherServices()
  {
    super();
  }

  @Override
  public void onCreate()
  {
    super.onCreate();

    currentState = STATE_NORMAL;
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId)
  {
    Log.i("weather"," + startId +  + intent");

    textGlass = intent.getExtras().getString("text");

    onServiceStart();
    return START_STICKY;
  }

  @Override
  public IBinder onBind(Intent intent)
  {
    // ????
    onServiceStart();
    return mBinder;
  }

  @Override
  public void onDestroy()
  {
    // ???
    onServiceStop();

    super.onDestroy();
  }


  // Service state handlers.
  // ....

  private boolean onServiceStart()
  {
    Log.d("weather","onServiceStart() called.");

    // TBD:
    // Publish live card...
    // ....
    // ....

    publishCard(this);
    currentState = STATE_NORMAL;
    return true;
  }

  private boolean onServicePause()
  {
    Log.d("weather","onServicePause() called.");
    return true;
  }
  private boolean onServiceResume()
  {
    Log.d("weather","onServiceResume() called.");
    return true;
  }

  private boolean onServiceStop()
  {
    Log.d("weather","onServiceStop() called.");

    // TBD:
    // Unpublish livecard here
    // .....
    unpublishCard(this);
    // ...

    return true;
  }


  // For live cards...

  private void publishCard(Context context)
  {
    Log.d("weather","publishCard() called.");
    if (liveCard == null) {
      String cardId = "livecarddemo_card";
      TimelineManager tm = TimelineManager.from(context);
      liveCard = tm.createLiveCard(cardId);


      RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
      R.layout.livecard_livecarddemo);
      remoteViews.setTextViewText(R.id.tvTempMin,  textGlass);


      liveCard.setViews(remoteViews);


      Intent intent = new Intent(context, CompassMenuActivity.class);
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);


      liveCard.setAction(PendingIntent.getActivity(this, 0, intent, 0));
      liveCard.publish(LiveCard.PublishMode.REVEAL);
    } else {
      // Card is already published.
      return;
    }
  }

  private void unpublishCard(Context context)
  {
    Log.d("weather","unpublishCard() called.");
    if (liveCard != null) {
      liveCard.unpublish();
      liveCard = null;
    }
  }




}