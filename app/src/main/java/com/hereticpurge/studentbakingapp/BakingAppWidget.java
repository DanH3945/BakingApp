package com.hereticpurge.studentbakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import timber.log.Timber;

public class BakingAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.widget_default);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);

        if (intent.hasExtra(DetailFragment.RECIPE_BROADCAST_INGREDIENT_STRING)){
            remoteViews.setTextViewText(R.id.appwidget_text,
                    intent.getStringExtra(DetailFragment.RECIPE_BROADCAST_INGREDIENT_STRING));
        }

        int selectedIndex = intent.getIntExtra(DetailFragment.RECIPE_BROADCAST_INDEX_ID, -1);

        Timber.d("Broadcast received with index of: " + selectedIndex);

        Intent wrappedIntent = new Intent(context, MainActivity.class);
        wrappedIntent.putExtra(DetailFragment.RECIPE_BROADCAST_INDEX_ID, selectedIndex);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, wrappedIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget_main, pendingIntent);

        ComponentName componentName = new ComponentName(context, BakingAppWidget.class);
        AppWidgetManager.getInstance(context).updateAppWidget(componentName, remoteViews);
        super.onReceive(context, intent);
    }
}

