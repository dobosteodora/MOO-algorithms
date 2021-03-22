package data;

import start.GUI;

public class Student {

    private String firstName;
    private String lastName;
    private String email;
    private String attribute_2;
    private String attribute_3;
    private String major;
    private int semester;
    private String language_a1;
    private String language_a2;
    private String iOSDev;
    private String appStoreLink;
    private String iOSDevExplained;
    private String introAssessment_INTRO;
    private String introAssessmentTutor_INTRO;
    private boolean devices_iPad;
    private boolean devices_iPhone;
    private boolean devices_Watch;
    private boolean devices_Mac;
    private boolean devices_iPadAR;
    private boolean devices_iPhoneAR;
    private String expinterWEBFE_expinter_1;
    private String expinterWEBFE_expinter_2;
    private String justifyWEBFE;
    private String expinterSSDEV_expinter_1;
    private String expinter_SSDEV_expinter_2;
    private String justifySSDEV;
    private String expinterUIUX_expinter_1;
    private String expinter_UIUX_expinter_2;
    private String justifyUIUX;
    private String expinterEMBEDED_expinter_1;
    private String expinterEMBEDED_expinter_2;
    private String justifyEMBEDED;
    private String expinterVRAR_expinter_1;

    public String getEmail() {
        return email;
    }

    public String getAttribute_2() {
        return attribute_2;
    }

    public String getAttribute_3() {
        return attribute_3;
    }

    public String getMajor() {
        return major;
    }

    public int getSemester() {
        return semester;
    }

    public String getLanguage_a1() {
        return language_a1;
    }

    public String getLanguage_a2() {
        return language_a2;
    }

    public String getiOSDev() {
        return iOSDev;
    }

    public String getAppStoreLink() {
        return appStoreLink;
    }

    public String getiOSDevExplained() {
        return iOSDevExplained;
    }

    public String getIntroAssessment_INTRO() {
        return introAssessment_INTRO;
    }

    public String getIntroAssessmentTutor_INTRO() {
        return introAssessmentTutor_INTRO;
    }

    public boolean isDevices_iPad() {
        return devices_iPad;
    }

    public boolean isDevices_iPhone() {
        return devices_iPhone;
    }

    public boolean isDevices_Watch() {
        return devices_Watch;
    }

    public boolean isDevices_Mac() {
        return devices_Mac;
    }

    public boolean isDevices_iPadAR() {
        return devices_iPadAR;
    }

    public boolean isDevices_iPhoneAR() {
        return devices_iPhoneAR;
    }

    public String getExpinterWEBFE_expinter_1() {
        return expinterWEBFE_expinter_1;
    }

    public String getExpinterWEBFE_expinter_2() {
        return expinterWEBFE_expinter_2;
    }

    public String getJustifyWEBFE() {
        return justifyWEBFE;
    }

    public String getExpinterSSDEV_expinter_1() {
        return expinterSSDEV_expinter_1;
    }

    public String getExpinter_SSDEV_expinter_2() {
        return expinter_SSDEV_expinter_2;
    }

    public String getJustifySSDEV() {
        return justifySSDEV;
    }

    public String getExpinterUIUX_expinter_1() {
        return expinterUIUX_expinter_1;
    }

    public String getExpinter_UIUX_expinter_2() {
        return expinter_UIUX_expinter_2;
    }

    public String getJustifyUIUX() {
        return justifyUIUX;
    }

    public String getExpinterEMBEDED_expinter_1() {
        return expinterEMBEDED_expinter_1;
    }

    public String getExpinterEMBEDED_expinter_2() {
        return expinterEMBEDED_expinter_2;
    }

    public String getJustifyEMBEDED() {
        return justifyEMBEDED;
    }

    public String getExpinterVRAR_expinter_1() {
        return expinterVRAR_expinter_1;
    }

    public String getExpinterVRAR_expinter_2() {
        return expinterVRAR_expinter_2;
    }

    public String getJustifyVRAR() {
        return justifyVRAR;
    }

    public String getExpinterMLALG_expinter_1() {
        return expinterMLALG_expinter_1;
    }

    public String getJustifyMLALG() {
        return justifyMLALG;
    }

    public String getOtherSkills() {
        return otherSkills;
    }

    public String[] getPriorities() {
        return priorities;
    }

    public String getComments() {
        return comments;
    }

    public String getSupervisorRating() {
        return supervisorRating;
    }

    public String getCommentsTutor() {
        return commentsTutor;
    }

    public boolean isPinned() {
        return isPinned;
    }

    public String getTeamName() {
        return teamName;
    }

    public int getIndex() {
        return index;
    }

    private String expinterVRAR_expinter_2;
    private String justifyVRAR;
    private String expinterMLALG_expinter_1;

    public String getExpinterMLALG_expinter_2() {
        return expinterMLALG_expinter_2;
    }

    private String expinterMLALG_expinter_2;
    private String justifyMLALG;
    private String otherSkills;
    private String[] priorities;
    private String comments;

    private String supervisorRating;

    //can be 1, 2, 3 or 4 (1: Expert, 4: Novice)
    private int instructorRating;

    private String commentsTutor;
    private boolean isPinned;
    private String teamName;

    private boolean isFemale;
    private int index;


    public Student(String firstName, String lastName, String email, String attribute_2, String attribute_3,
                   String major, int semester, String language_a1, String language_a2, String iOSDev, String appStoreLink,
                   String iOSDevExplained, String introAssessment_INTRO, String introAssessmentTutor_INTRO,
                   boolean devices_iPad, boolean devices_iPhone, boolean devices_Watch, boolean devices_Mac, boolean devices_iPadAR,
                   boolean devices_iPhoneAR, String expinterWEBFE_expinter_1, String expinterWEBFE_expinter_2,
                   String justifyWEBFE, String expinterSSDEV_expinter_1, String expinterSSDEV_expinter_2,
                   String justifySSDEV, String expinterUIUX_expinter_1, String expinterUIUX_expinter_2, String justifyUIUX,
                   String expinterEMBEDED_expinter_1, String expinterEMBEDED_expinter_2, String justifyEMBEDED,
                   String expinterVRAR_expinter_1, String expinterVRAR_expinter_2, String justifyVRAR,
                   String expinterMLALG_expinter_1, String expinterMLALG_expinter_2, String justifyMLALG,
                   String otherSkills, String[] priorities, String comments, String supervisorRating, String commentsTutor,
                   boolean isPinned, String teamName){

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.attribute_2 = attribute_2;

        //male or female
        this.attribute_3 = attribute_3;
        if(attribute_3.equals("male")){
            isFemale = false;
        }else if(attribute_3.equals("female")){
            isFemale = true;
        }

        this.major = major;
        this.semester = semester;
        this.language_a1 = language_a1;
        this.language_a2 = language_a2;
        this.iOSDev = iOSDev;
        this.appStoreLink = appStoreLink;
        this.iOSDevExplained = iOSDevExplained;
        this.introAssessment_INTRO = introAssessment_INTRO;
        this.introAssessmentTutor_INTRO = introAssessmentTutor_INTRO;
        this.devices_iPad = devices_iPad;
        this.devices_iPhone = devices_iPhone;
        this.devices_Watch = devices_Watch;
        this.devices_Mac = devices_Mac;
        this.devices_iPadAR = devices_iPadAR;
        this.devices_iPhoneAR = devices_iPhoneAR;
        this.expinterWEBFE_expinter_1 = expinterWEBFE_expinter_1;
        this.expinterWEBFE_expinter_2 = expinterWEBFE_expinter_2;
        this.justifyWEBFE = justifyWEBFE;
        this.expinterSSDEV_expinter_1 = expinterSSDEV_expinter_1;
        this.expinter_SSDEV_expinter_2 = expinterSSDEV_expinter_2;
        this.justifySSDEV = justifySSDEV;
        this.expinterUIUX_expinter_1 = expinterUIUX_expinter_1;
        this.expinter_UIUX_expinter_2 = expinterUIUX_expinter_2;
        this.justifyUIUX = justifyUIUX;
        this.expinterEMBEDED_expinter_1 = expinterEMBEDED_expinter_1;
        this.expinterEMBEDED_expinter_2 = expinterEMBEDED_expinter_2;
        this.justifyEMBEDED = justifyEMBEDED;
        this.expinterVRAR_expinter_1 = expinterVRAR_expinter_1;
        this.expinterVRAR_expinter_2 = expinterVRAR_expinter_2;
        this.justifyVRAR = justifyVRAR;
        this.expinterMLALG_expinter_1 = expinterMLALG_expinter_1;
        this.expinterMLALG_expinter_2 = expinterMLALG_expinter_2;
        this.justifyMLALG = justifyMLALG;
        this.otherSkills = otherSkills;
        this.priorities = priorities;
        this.comments = comments;

        this.supervisorRating = supervisorRating;

        if(supervisorRating.equals("Expert")){
            this.instructorRating = 1;
        }else if(supervisorRating.equals("Advanced")){
            this.instructorRating = 2;
        }else if(supervisorRating.equals("Normal")){
            this.instructorRating = 3;
        }else if(supervisorRating.equals("Novice")){
            this.instructorRating = 4;
        }

        this.commentsTutor = commentsTutor;
        this.isPinned = isPinned;
        this.teamName = teamName;
    }

    public String getFirstName(){
        return  firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public int getPriorityForTeam(int teamIndex){

        for(int i = 0; i < GUI.teams.size(); i++){
            if(i == teamIndex){
                for(int j = 0; j < priorities.length; j++){
                    if(priorities[j].equals(GUI.teams.get(teamIndex).getName())){
                        return j + 1;
                    }
                }
            }
        }
        return -1;
    }

    public int getInstructorRating(){
        return instructorRating;
    }

    public boolean isFemale() {
        return isFemale;
    }

    public boolean isHasMac() {
        return devices_Mac;
    }

    public boolean isHasIDevice() {
        return devices_iPad || devices_iPadAR || devices_iPhoneAR || devices_iPhone || devices_Watch;
    }

    @Override
    public String toString() {
        String result = "data.data.Student " + this.firstName + " " + this.lastName;
        return result;
    }
}
