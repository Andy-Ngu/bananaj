/**
 * @author alexanderweiss
 * @date 06.11.2015
 */
package model.list.member;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


import connection.MailchimpConnection;
import exceptions.EmailException;
import org.apache.commons.validator.EmailValidator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import exceptions.EmailException;
import model.MailchimpObject;
import model.list.List;


/**
 * Object for representing a mailchimp member
 * @author alexanderweiss
 *
 */
public class Member extends MailchimpObject{

	private List list;
    private HashMap<String, Object> merge_fields;
	private String unique_email_id;
	private String email_address;
	private MemberStatus status;
	private String timestamp_signup;
	private String timestamp_opt;
	private double avg_open_rate;
	private double avg_click_rate;
	private String last_changed;
	private ArrayList<MemberActivity> memberActivities;
	private MailchimpConnection connection;

	


	public Member(String id, List list, HashMap<String, Object> merge_fields, String unique_email_id, String email_address, MemberStatus status, String timestamp_signup, String timestamp_opt, double avg_open_rate, double avg_click_rate, String last_changed, MailchimpConnection connection, JSONObject jsonRepresentation){
		//TODO Add merge field support
        super(id,jsonRepresentation);
		setList(list);
        setMerge_fields(merge_fields);
		setUnique_email_id(unique_email_id);
		setEmail_address(email_address);
		setStatus(status);
		setTimestamp_signup(timestamp_signup);
		setTimestamp_opt(timestamp_opt);
		setAvg_open_rate(avg_open_rate);
		setAvg_click_rate(avg_click_rate);
		setLast_changed(last_changed);
		setConnection(connection);

		try{
			setMemberActivities(unique_email_id, list.getId());
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Update the status of a member manually
	 * @param status
	 * @throws Exception
	 */
	public void updateStatus(MemberStatus status) throws Exception{
		JSONObject updateMember = new JSONObject();
		updateMember.put("status", status.getStringRepresentation());
		this.getConnection().do_Post(new URL("https://"+list.getConnection().getServer()+".api.mailchimp.com/3.0/lists/"+getList().getId()+"/members/"+getId()), updateMember.toString());
	}
	
	/**
	 * Update the list of this member
	 * @param listId
	 * @throws Exception 
	 */
	public void changeList(String listId) throws Exception{
		JSONObject updateMember = new JSONObject();
		updateMember.put("list_id", listId);
        this.getConnection().do_Post(new URL("https://"+list.getConnection().getServer()+".api.mailchimp.com/3.0/lists/"+getList().getId()+"/members/"+getId()), updateMember.toString());
	}
	
	/**
	 * Update the email adress of this memeber
	 * @param emailAdress
	 * @throws Exception
	 */
	public void changeEmailAdress(String emailAdress) throws EmailException, JSONException, MalformedURLException, Exception{
		
		EmailValidator validator = EmailValidator.getInstance();
		if (validator.isValid(emailAdress)) {
			String url = "https://"+list.getConnection().getServer()+".api.mailchimp.com/3.0/lists/"+getList().getId()+"/members/"+this.getId();
			JSONObject updateMember = new JSONObject();
			updateMember.put("email_adress", emailAdress);
            this.getConnection().do_Post(new URL("https://"+list.getConnection().getServer()+".api.mailchimp.com/3.0/lists/"+getList().getId()+"/members/"+getId()), updateMember.toString());

		} else {
		   throw new EmailException("Email adress is not valid");
		}
	}

	/**
	 * Update the email adress of this memeber
	 * @param status
	 * @throws Exception
	 */
	public void changeMemberStatus(MemberStatus status) throws Exception{

		String url = "https://"+list.getConnection().getServer()+".api.mailchimp.com/3.0/lists/"+getList().getId()+"/members/"+this.getId();
		JSONObject updateMember = new JSONObject();
		updateMember.put("status", status.getStringRepresentation());
		this.getConnection().do_Post(new URL("https://"+list.getConnection().getServer()+".api.mailchimp.com/3.0/lists/"+getList().getId()+"/members/"+getId()), updateMember.toString());
	}

	/**
	 * @return the unique_email_id
	 */
	public String getUnique_email_id() {
		return unique_email_id;
	}
	/**
	 * @param unique_email_id the unique_email_id to set
	 */
	public void setUnique_email_id(String unique_email_id) {
		this.unique_email_id = unique_email_id;
	}
	/**
	 * @return the email_adress
	 */
	public String getEmail_address() {
		return email_address;
	}
	/**
	 * @param email_address the email_adress to set
	 */
	public void setEmail_address(String email_address) {
		this.email_address = email_address;
	}
	/**
	 * @return the status
	 */
	public MemberStatus getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(MemberStatus status) {
		this.status = status;
	}
	/**
	 * @return the timestamp_signup
	 */
	public String getTimestamp_signup() {
		return timestamp_signup;
	}
	/**
	 * @param timestamp_signup the timestamp_signup to set
	 */
	public void setTimestamp_signup(String timestamp_signup) {
		this.timestamp_signup = timestamp_signup;
	}
	/**
	 * @return the timestamp_opt
	 */
	public String getTimestamp_opt() {
		return timestamp_opt;
	}
	/**
	 * @param timestamp_opt the timestamp_opt to set
	 */
	public void setTimestamp_opt(String timestamp_opt) {
		this.timestamp_opt = timestamp_opt;
	}
	
	@Override
	public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        Iterator it = getMerge_fields().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            stringBuilder.append(pair.getKey()+ ": " + pair.getValue() +  "\n");
            it.remove(); // avoids a ConcurrentModificationException
        }

		return System.lineSeparator()+"ID: " + this.getId() + "\t"+  System.getProperty("line.separator")
				+ "Unique email adress: " + this.getUnique_email_id() + System.getProperty("line.separator")
				+ "Email address: " + this.getEmail_address() + System.getProperty("line.separator") 
				+ "Status: " + this.getStatus().getStringRepresentation() + System.getProperty("line.separator") 
				+ "Sign_Up: " + this.getTimestamp_signup() + System.getProperty("line.separator")
				+ "Opt_In: " + this.getTimestamp_opt() + System.lineSeparator()
				+ "Last changed: " + this.getLast_changed() + System.lineSeparator()
                + stringBuilder.toString()
                + "_________________________________________________";
	}

	/**
	 * @return the avg_open_rate
	 */
	public double getAvg_open_rate() {
		return avg_open_rate;
	}


	/**
	 * @param avg_open_rate the avg_open_rate to set
	 */
	public void setAvg_open_rate(double avg_open_rate) {
		this.avg_open_rate = avg_open_rate;
	}


	/**
	 * @return the avg_click_rate
	 */
	public double getAvg_click_rate() {
		return avg_click_rate;
	}


	/**
	 * @param avg_click_rate the avg_click_rate to set
	 */
	public void setAvg_click_rate(double avg_click_rate) {
		this.avg_click_rate = avg_click_rate;
	}


	/**
	 * @return the listId
	 */
	public List getList() {
		return list;
	}


	/**
	 * @param list the list to set
	 */
	public void setList(List list) {
		this.list = list;
	}

	/**
	 * @return the last_changed
	 */
	public String getLast_changed() {
		return last_changed;
	}

	/**
	 * @param last_changed the last_changed to set
	 */
	public void setLast_changed(String last_changed) {
		this.last_changed = last_changed;
	}

	public void setMemberActivities(String unique_email_id, String listID) throws Exception{
		ArrayList<MemberActivity> activities = new ArrayList<MemberActivity>();

		final JSONObject activity = new JSONObject(this.getConnection().do_Get(new URL("https://"+this.list.getConnection().getServer()+".api.mailchimp.com/3.0/lists/"+this.list.getId()+"/members/"+this.getId()+"/activity")));
		final JSONArray activityArray = activity.getJSONArray("activity");

		for (int i = 0 ; i < activityArray.length();i++)
		{
			final JSONObject activityDetail = activityArray.getJSONObject(i);
			MemberActivity memberActivity = new MemberActivity(this.unique_email_id, this.list.getId(), activityDetail.getString("action"),activityDetail.getString("timestamp"), activityDetail.getString("campaign_id"), activityDetail.getString("title"));
			activities.add(memberActivity);
		}

		this.memberActivities = activities;


	}

	public ArrayList<MemberActivity> getMemberActivities(){
		return this.memberActivities;
	}

	public MailchimpConnection getConnection() {
		return connection;
	}

	public void setConnection(MailchimpConnection connection) {
		this.connection = connection;
	}

    public HashMap<String, Object> getMerge_fields() {
        return merge_fields;
    }

    public void setMerge_fields(HashMap<String, Object> merge_fields) {
        this.merge_fields = merge_fields;
    }
}
