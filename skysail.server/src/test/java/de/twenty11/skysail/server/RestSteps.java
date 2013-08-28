package de.twenty11.skysail.server;

import de.twenty11.skysail.server.internal.DefaultSkysailApplication;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.pages.Pages;
import net.thucydides.core.steps.ScenarioSteps;
import org.restlet.data.Form;
import org.restlet.resource.ClientResource;

public class RestSteps extends ScenarioSteps {

	private static final long serialVersionUID = -4407369914748239096L;

	public RestSteps(Pages pages) {
		super(pages);
	}


	@Step
	public void createFolder() {
		
	}

	@Step
	public String login(String username, String password) throws Exception {
		ClientResource cr = new ClientResource(requestUrlFor(DefaultSkysailApplication.LOGIN_PATH));
        Form form = new Form();
        form.add("username", username);
        form.add("password", password);
        return cr.post(form).getText();
	}

//	@Step
//	public String postFolder(String folderName) throws Exception {
//        ClientResource cr = new ClientResource(requestUrlFor(NotesApplication.FOLDERS_PATH + "?debug=true"));
//        Form form = new Form();
//        form.add("folderName", folderName);
//        return cr.post(form).getText();
//	}
//
//    @Step
//	public String deleteFolder(Integer folderId) throws Exception {
//        ClientResource cr = new ClientResource(requestUrlFor(NotesApplication.FOLDERS_PATH + "/" + folderId));
//        return cr.delete().getText();
//	}
//
//    @Step
//	public String getFolder(Integer folderId) throws Exception {
//        ClientResource cr = new ClientResource(requestUrlFor(NotesApplication.FOLDERS_PATH + "/" + folderId));
//        return cr.get().getText();
//	}

    protected String requestUrlFor(String resource) {
        return "http://localhost:" + ResourceTestWithUnguardedAppication.TEST_PORT + resource;
    }


}
