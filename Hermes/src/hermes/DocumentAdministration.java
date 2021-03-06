package hermes;

import hermes.datastructures.DocSession;
import hermes.view.View;
import hermes.view.document.DocumentSessionView;

import java.util.HashMap;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.muc.MultiUserChat;

public class DocumentAdministration {

    private final Controller controller;
    private HashMap<String, DocSession> currentMucs;
    public static DocumentAdministration CURRENT_INSTANCE;

    public DocumentAdministration(Controller controller) {
	CURRENT_INSTANCE = this;
	this.controller = controller;
	this.currentMucs = new HashMap<String, DocSession>();
    }

    public void initDocEditing(String with) {

	DocSession session = createSession();
	session.localName = "New File";
	session.sessionView = new DocumentSessionView(session);
	View.CURRENT_INSTANCE.addDocument(session.sessionView);
	this.currentMucs.put(session.muc.getRoom(), session);

    }

    public void editExistingFileWith(String user) {
	// TODO implement this method
    }

    public void addUserToSession(DocSession session, String user) {
	// TODO implement this method
    }

    public DocSession createSession() {
	MultiUserChat muc;
	do {

	    String mucName = "hermes."
		    + controller.getThisUser()
		    + (System.currentTimeMillis() / 1000 + "@conference.jabber.org");
	    muc = new MultiUserChat(controller.conn, mucName);

	    try {
		muc.create(controller.getThisUser());
		muc.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
	    } catch (XMPPException e) {
		e.printStackTrace();
		continue;
	    }
	} while (!muc.isJoined());

	DocSession s = new DocSession(muc);
	s.masterMember = controller.getThisUser();
	s.members.add(s.masterMember);
	return s;
    }

    public HashMap<String, DocSession> getOpenSessions() {
	return this.currentMucs;
    }
}
