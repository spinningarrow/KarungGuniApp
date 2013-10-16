package com.onemore.karungguniapp;

import com.facebook.Request;
import com.facebook.Session;
import com.facebook.widget.WebDialog;
import com.facebook.*;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


public final class FacebookUtil {

public static void postToFacebook(final Activity _context, final String _name, final String _caption,
        final String _description, final String _link, final String _imageUrl) {
    Session s = Session.getActiveSession();
    if (s != null && s.isOpened()) {
        post(_context, _name, _caption, _description, _link, _imageUrl);
    } else {
        Session.openActiveSession(_context, true, new Session.StatusCallback() {

            @Override
            public void call(Session _session, SessionState _state, Exception _exception) {
                if (_session.isOpened()) {
                    post(_context, _name, _caption, _description, _link, _imageUrl);
                }
            }
        });
    }
}


private static void post(Context _context, String _name, String _caption, String _description, String _link,
        String _imageUrl) {
    Bundle params = new Bundle();
    params.putString("name", _name);
    params.putString("caption", _caption);
    params.putString("description", _description);
    params.putString("link", _link);
    if (_imageUrl != null) {
        params.putString("picture", _imageUrl);
    }
    new WebDialog.FeedDialogBuilder(_context, Session.getActiveSession(), params).build().show();
}


public static void login(Activity _context, Session.StatusCallback _callback) {
    Session.openActiveSession(_context, true, _callback);
}


public static void logout() {
    Session session = Session.getActiveSession();
    if (session != null) {
        session.closeAndClearTokenInformation();
    }
}


public static boolean isLoggedIn() {
    Session session = Session.getActiveSession();
    return (session != null && session.getAccessToken() != null && session.getAccessToken().length() > 1);
}


public static void askMe(Request.GraphUserCallback _callback) {
    Session session = Session.getActiveSession();
    if (session != null) {
        Request.executeMeRequestAsync(session, _callback);
    }
}


/**
 * Don't forget to added this function to onActivityResult() of your
 * activities ever, otherwise you can not finish your facebook successfully.
 */
public static void onActivityResult(Activity _activity, int _requestCode, int _resultCode, Intent _data) {
    Session.getActiveSession().onActivityResult(_activity, _requestCode, _resultCode, _data);
}
}
