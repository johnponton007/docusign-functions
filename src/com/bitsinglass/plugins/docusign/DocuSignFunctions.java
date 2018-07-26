package com.bitsinglass.plugins.docusign;

import com.appiancorp.suiteapi.expression.annotations.Category;
import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
import com.docusign.esign.api.*;
import com.docusign.esign.client.*;
import com.docusign.esign.model.*;
/*import java.awt.Desktop;

import java.io.IOException;
import java.net.URI;*/

import org.apache.oltu.oauth2.common.exception.OAuthSystemException;

@Category("docuSignCategory")
public class DocuSignFunctions {
	
	/*public static void main(String[] args){
		
		initiateDocuSignProcess();
		//getEmbeddedSigningURL("eyJ0eXAiOiJNVCIsImFsZyI6IlJTMjU2Iiwia2lkIjoiNjgxODVmZjEtNGU1MS00Y2U5LWFmMWMtNjg5ODEyMjAzMzE3In0.AQgAAAABAAYABwAAnaLsnvHVSAgAACkpNJ_x1UgCAE3KLKvosN9Mg3K75bHEJRkVAAEAAAAYAAEAAAAFAAAADQAkAAAANmQzMzUzZjgtZGQzNC00NTAxLTg4YzEtNGE0M2I5ZWUxZWU0MAAAH4R8fvHVSA.fukZDsB6VPiaAlnZwepy98JjpRw1cTGp09q50BJ-9yN5zTKcfIkGUxAKHBgsgYdFlpjCivcCyxM-7Mf7JGogpUEVUQABK6ZIWRohzavqUs1syE4eZi4aXoEGIyiVPNk0QbqPJVDwClGMoWrRBzMsctN780z11UxMIOxy-hxAZ7POSmmPvLYGzO1b98GAYn_jT1PkXzpXh1iWxk7BMqUh7oqYRGjDHIg0nl8YVcoqGB0zhaPI73F3-OLGFYhQwa8HQDKUpSxNJRRQg6NVD7bX9tqoqVPisjIpRX0kf9YspgUj9QU0WvZ7rJKSPSNZRY32Hg-FZVxID8ZsKYQ-EvtAmA");
	}*/

	@Function
	public String initiateDocuSignProcess() {

		String IntegratorKey = "6d3353f8-dd34-4501-88c1-4a43b9ee1ee4";

		String ClientSecret = "b6f41080-4f1f-48fe-8157-c178912fb1e2";

		// must match a redirect URI (case-sensitive) you configured on the key
		String RedirectURI = "https://big-appian-dev-01.bitsinglass.com/suite/webapi/udl2nw";

		String AuthServerUrl = "https://account-d.docusign.com";

		// point to the demo (sandbox) environment. For production requests your
		// account sub-domain
		// will vary, you should always use the base URI that is returned from
		// authentication to
		// ensure your integration points to the correct endpoints (in both
		// environments)
		String RestApiUrl = "https://demo.docusign.net/restapi";

		// instantiate the api client and configure auth server
		ApiClient apiClient = new ApiClient(AuthServerUrl, "docusignAccessCode", IntegratorKey, ClientSecret);

		// set the base path for REST API requests
		apiClient.setBasePath(RestApiUrl);

		// configure the authorization flow on the api client
		apiClient.configureAuthorizationFlow(IntegratorKey, ClientSecret, RedirectURI);

		// set as default api client in your configuration
		Configuration.setDefaultApiClient(apiClient);

		String oauthLoginUrl = "";

		try {
			// get DocuSign OAuth authorization url
			oauthLoginUrl = apiClient.getAuthorizationUri();
			// open DocuSign OAuth login in the browser
			System.out.println(oauthLoginUrl);
		} catch (OAuthSystemException ex) {
			System.out.println(ex);
		}
		
		/*try {
			Desktop.getDesktop().browse(URI.create(oauthLoginUrl));
		} catch (IOException e) {
			e.printStackTrace();
		}*/

		return oauthLoginUrl;
	}

	@Function
	public String getEmbeddedSigningURL(@Parameter String accessCode) {

		// Reconfigure the API Client

		String IntegratorKey = "6d3353f8-dd34-4501-88c1-4a43b9ee1ee4";

		String ClientSecret = "b6f41080-4f1f-48fe-8157-c178912fb1e2";

		// must match a redirect URI (case-sensitive) you configured on the key
		String RedirectURI = "https://big-appian-dev-01.bitsinglass.com/suite/webapi/udl2nw";

		String AuthServerUrl = "https://account-d.docusign.com";

		// point to the demo (sandbox) environment. For production requests your
		// account sub-domain
		// will vary, you should always use the base URI that is returned from
		// authentication to
		// ensure your integration points to the correct endpoints (in both
		// environments)
		String RestApiUrl = "https://demo.docusign.net/restapi";

		// instantiate the api client and configure auth server
		ApiClient apiClient = new ApiClient(AuthServerUrl, "docusignAccessCode", IntegratorKey, ClientSecret);

		// set the base path for REST API requests
		apiClient.setBasePath(RestApiUrl);

		// configure the authorization flow on the api client
		apiClient.configureAuthorizationFlow(IntegratorKey, ClientSecret, RedirectURI);

		// set access tokens in the API Client
		String code = accessCode;
		// assign it to the token endpoint
		apiClient.getTokenEndPoint().setCode(code);

		// following call exchanges the authorization code for an access code
		// and updates
		// the `Authorization: bearer <token>` header on the api client
		apiClient.updateAccessToken();

		// get account ID
		AuthenticationApi authApi = new AuthenticationApi(apiClient);
		LoginInformation loginInfo = null;
		try {
			loginInfo = authApi.login();
		} catch (ApiException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// that note user might belong to multiple accounts, here we get first
		String accountId = loginInfo.getLoginAccounts().get(0).getAccountId();
		
		String email = loginInfo.getLoginAccounts().get(0).getEmail();
		
		String userName = loginInfo.getLoginAccounts().get(0).getUserName();
		
		String name = loginInfo.getLoginAccounts().get(0).getName();

		// hopefully create the envelope and shiz

		// create a new envelope to manage the signature request
		EnvelopeDefinition envDef = new EnvelopeDefinition();
		envDef.setEmailSubject("DocuSign Java SDK - Sample Signature Request");

		// assign template information including ID and role(s)
		envDef.setTemplateId("8d6b5f70-243b-4ab9-941e-02a7d7647291");

		// create a template role with a valid templateId and roleName and
		// assign signer info
		TemplateRole tRole = new TemplateRole();
		
		loginInfo.getLoginAccounts();
		tRole.setRoleName("Signator");
		tRole.setName(userName);
		tRole.setEmail(email);

		// set the clientUserId on the recipient to mark them as embedded (ie we
		// will generate their signing link)
		tRole.setClientUserId("1001");

		// create a list of template roles and add our newly created role
		java.util.List<TemplateRole> templateRolesList = new java.util.ArrayList<TemplateRole>();
		templateRolesList.add(tRole);

		// assign template role(s) to the envelope
		envDef.setTemplateRoles(templateRolesList);

		// send the envelope by setting |status| to "sent". To save as a draft
		// set to "created"
		envDef.setStatus("sent");

		// instantiate a new EnvelopesApi object
		EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);

		// call the createEnvelope() API
		String envelopeId = null;
		try {
			EnvelopeSummary envelopeSummary = envelopesApi.createEnvelope(accountId, envDef);
			envelopeId = envelopeSummary.getEnvelopeId();
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		RecipientViewRequest view = new RecipientViewRequest();

		view.setReturnUrl("https://big-appian-dev-01.bitsinglass.com/suite/tempo/tasks/assignedtome");

		view.setAuthenticationMethod("email");

		// recipient information must match embedded recipient info we provided
		// in step #2
		view.setEmail(email);
		view.setUserName(userName);
		view.setRecipientId("1");
		view.setClientUserId("1001");

		// call the CreateRecipientView API
		ViewUrl recipientView = null;
		try {
			recipientView = envelopesApi.createRecipientView(accountId, envelopeId, view);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(recipientView.getUrl());
		
		System.out.println(email);
		
		System.out.println(userName);
		
		System.out.println(name);
		
		return recipientView.getUrl();

	}

}
