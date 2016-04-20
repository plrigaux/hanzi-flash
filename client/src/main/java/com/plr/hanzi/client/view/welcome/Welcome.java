package com.plr.hanzi.client.view.welcome;

import com.google.api.gwt.oauth2.client.Auth;
import com.google.api.gwt.oauth2.client.AuthRequest;
import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.plr.hanzi.client.ApplicationConst;

public class Welcome extends Composite implements ApplicationConst {

	private static WelcomeUiBinder uiBinder = GWT.create(WelcomeUiBinder.class);

	interface WelcomeUiBinder extends UiBinder<Widget, Welcome> {
	}

	@UiField
	CustomButton about;

	@UiField
	CustomButton flashcards;
	@UiField
	CustomButton guesser;
	@UiField
	CustomButton browser;

	public Welcome() {
		initWidget(uiBinder.createAndBindUi(this));

		// browser.setText(HanziConstants.INSTANCE.charBrowser());
	}

	@UiHandler("about")
	void onAboutClick(ClickEvent event) {
		alertWidget("About", "Developed by Pier Rigaux plrigaux@gmail.com based on the CEDICT").center();
	}

	@UiHandler("flashcards")
	void onFlashcardsClick(ClickEvent event) {
		History.newItem(FLASH);
	}

	@UiHandler("browser")
	void onBrowserClick(ClickEvent event) {
		History.newItem(DICTIONNARY);
	}

	@UiHandler("guesser")
	void onGuesserClick(ClickEvent event) {
		History.newItem(SHI_SHENME);
	}

	private static final String GOOGLE_AUTH_URL = "https://accounts.google.com/o/oauth2/auth";

	// This app's personal client ID assigned by the Google APIs Console
	// (http://code.google.com/apis/console).
	//private static final String GOOGLE_CLIENT_ID = "940062849852.apps.googleusercontent.com";

	private static final String GOOGLE_CLIENT_ID = "940062849852-8s3kbq4eli2ovh71mikh9rlniuh98hlv.apps.googleusercontent.com";
	
	
	// The auth scope being requested. This scope will allow the application to
	// read Buzz activities, comments, etc., as if it was the user.
	private static final String BUZZ_READONLY_SCOPE = "https://www.googleapis.com/auth/buzz.readonly";

	// Use the implementation of Auth intended to be used in the GWT client app.
	;

	@UiHandler("login")
	void onLoginClick(ClickEvent event) {

		final Auth AUTH = Auth.get();

		final AuthRequest req = new AuthRequest(GOOGLE_AUTH_URL, GOOGLE_CLIENT_ID).withScopes(BUZZ_READONLY_SCOPE);

		// Calling login() will display a popup to the user the first time it is
		// called. Once the user has granted access to the application,
		// subsequent calls to login() will not display the popup, and will
		// immediately result in the callback being given the token to use.
		AUTH.login(req, new Callback<String, Throwable>() {
			@Override
			public void onSuccess(String token) {
				Window.alert("Got an OAuth token:\n" + token + "\n" + "Token expires in " + AUTH.expiresIn(req) + " ms\n");
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error:\n" + caught.getMessage());
			}
		});
	}

	// //////////////////////////////////////////////////////////////////////////
	// AUTHENTICATING WITH FACEBOOK
	// /////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////

	private static final String FACEBOOK_AUTH_URL = "https://www.facebook.com/dialog/oauth";

	// This app's personal client ID assigned by the Facebook Developer App
	// (http://www.facebook.com/developers).
	private static final String FACEBOOK_CLIENT_ID = "129996937108913";

	// All available scopes are listed here:
	// http://developers.facebook.com/docs/authentication/permissions/
	// This scope allows the app to access the user's email address.
	private static final String FACEBOOK_EMAIL_SCOPE = "email";

	// This scope allows the app to access the user's birthday.
	private static final String FACEBOOK_BIRTHDAY_SCOPE = "user_birthday";

	// Adds a button to the page that asks for authentication from Facebook.
	// Note that Facebook does not allow localhost as a redirect URL, so while
	// this code will work when hosted, it will not work when testing locally.

	// Since the auth flow requires opening a popup window, it must be started
	// as a direct result of a user action, such as clicking a button or link.
	// Otherwise, a browser's popup blocker may block the popup.

	@UiHandler("facebook")
	public void onFacebookClick(ClickEvent event) {
		final Auth AUTH = Auth.get();
		final AuthRequest req = new AuthRequest(FACEBOOK_AUTH_URL, FACEBOOK_CLIENT_ID).withScopes(FACEBOOK_EMAIL_SCOPE,
				FACEBOOK_BIRTHDAY_SCOPE)
		// Facebook expects a comma-delimited list of scopes
				.withScopeDelimiter(",");
		AUTH.login(req, new Callback<String, Throwable>() {
			@Override
			public void onSuccess(String token) {
				Window.alert("Got an OAuth token:\n" + token + "\n" + "Token expires in " + AUTH.expiresIn(req) + " ms\n");
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error:\n" + caught.getMessage());
			}
		});
	}

	public static DialogBox alertWidget(final String header, final String content) {
		final DialogBox box = new DialogBox();
		final VerticalPanel panel = new VerticalPanel();
		box.setText(header);
		panel.add(new Label(content));
		final Button buttonClose = new Button("Close", new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				box.hide();
			}
		});
		// few empty labels to make widget larger
		final Label emptyLabel = new Label("");
		emptyLabel.setSize("auto", "25px");
		panel.add(emptyLabel);
		panel.add(emptyLabel);
		buttonClose.setWidth("90px");
		panel.add(buttonClose);
		panel.setCellHorizontalAlignment(buttonClose, HasAlignment.ALIGN_RIGHT);
		box.add(panel);
		box.setGlassEnabled(true);
		return box;
	}
}
