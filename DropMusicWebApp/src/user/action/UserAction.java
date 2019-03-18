package user.action;

import com.opensymphony.xwork2.ActionSupport;
import login.model.LoginBean;
import org.apache.struts2.interceptor.SessionAware;
import user.model.*;

import java.util.Map;

public class UserAction extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;
    private String username;
    final String NETWORK_NAME = "DropBox";
    private String authorizationUrl = null;

    private String DropboxUrl;
    final String PROTECTED_RESOURCE_URL = "https://www.dropbox.com/oauth2/authorize";
    private DropBoxBean dropBoxBean;

    private String code;


    public String execute() {
        Boolean permission;
        permission = (Boolean) session.get("LOGGED_IN");
        if (permission != null && permission) {
            return SUCCESS;
        }
        return ERROR;
    }

    public String dropBroxAssociation() {
        Boolean permission;

        permission = (Boolean) session.get("LOGGED_IN");
        System.out.println(permission);
        if (permission != null) {
            this.DropboxUrl = this.getDropBoxBean().getUrlDropbox();
            System.out.println(this.DropboxUrl);
            return SUCCESS;

        }

        return LOGIN;
    }


    public String dropboxAssociation2() {
        setUsername((String) ((LoginBean) session.get("loginBean")).getUsername());

        System.out.println(this.username);
        System.out.println(getCode());
        boolean flag = getDropBoxBean().dropboxAssociation(this.username, getCode());
        if (flag) {
            return SUCCESS;
        } else {

            return ERROR;

        }

    }


    public ArtistBean getArtistBean() {
        if (!session.containsKey("artistBean"))
            this.setArtistBean(new ArtistBean());
        return (ArtistBean) session.get("artistBean");
    }

    public void setArtistBean(ArtistBean artistBean) {
        this.session.put("artistBean", artistBean);
    }


    public AlbumBean getAlbumBean() {
        if (!session.containsKey("albumBean"))
            this.setAlbumBean(new AlbumBean());
        return (AlbumBean) session.get("albumBean");
    }

    public void setAlbumBean(AlbumBean albumBean) {
        this.session.put("albumBean", albumBean);
    }

    public MusicBean getMusicBean() {
        if (!session.containsKey("musicBean"))
            this.setMusicBean(new MusicBean());
        return (MusicBean) session.get("musicBean");
    }

    public void setMusicBean(MusicBean musicBean) {
        this.session.put("musicBean", musicBean);
    }

    public UserBean getUserBean() {
        if (!session.containsKey("userBean"))
            this.setUserBean(new UserBean());
        return (UserBean) session.get("userBean");
    }

    public void setUserBean(UserBean userBean) {
        this.session.put("userBean", userBean);
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }


    public DropBoxBean getDropBoxBean() {
        if (!session.containsKey("dropboxBean"))
            this.setDropboxBean(new DropBoxBean());
        return (DropBoxBean) session.get("dropboxBean");
    }

    public void setDropboxBean(DropBoxBean dropboxBean) {
        this.session.put("dropboxBean", dropboxBean);
    }

    public String getDropboxUrl() {
        return DropboxUrl;
    }

    public void setDropboxUrl(String dropboxUrl) {
        DropboxUrl = dropboxUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
