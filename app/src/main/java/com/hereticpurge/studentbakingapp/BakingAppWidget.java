package com.hereticpurge.studentbakingapp;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

public class BakingAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
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

        if (intent.hasExtra(DetailFragment.RECIPE_BROADCAST_ID)){
            Toast.makeText(context, "got it", Toast.LENGTH_LONG).show();
        }

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
        remoteViews.setTextViewText(R.id.appwidget_text, "It worked");
        ComponentName componentName = new ComponentName(context, BakingAppWidget.class);
        AppWidgetManager.getInstance(context).updateAppWidget(componentName, remoteViews);
        super.onReceive(context, intent);
    }
}

