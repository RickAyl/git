package com.cqsmiletv.game;

import android.os.Parcel;
import android.os.Parcelable;

public class OneUpJoyGameAccount implements Parcelable{
	
    /**
     * userId : 510800627
     * cpId : LS_65751080063
     * loginName : 45084630
     * pwd : 888888
     * flag : success
     */

    private String userId;
    private String cpId;
    private String loginName;
    private String pwd;
    private String flag;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setCpId(String cpId) {
        this.cpId = cpId;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getUserId() {
        return userId;
    }

    public String getCpId() {
        return cpId;
    }

    public String getLoginName() {
        return loginName;
    }

    public String getPwd() {
        return pwd;
    }

    public String getFlag() {
        return flag;
    }

	@Override
	public String toString() {
		return "OneUpJoyGameAccount [userId=" + userId + ", cpId=" + cpId
				+ ", loginName=" + loginName + ", pwd=" + pwd + ", flag="
				+ flag + "]";
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub\
		dest.writeString(userId);
		dest.writeString(cpId);
		dest.writeString(loginName);
		dest.writeString(pwd);
		dest.writeString(flag);
	}
	
	public static final Parcelable.Creator<OneUpJoyGameAccount> CREATOR=new Creator<OneUpJoyGameAccount>() {

		@Override
		public OneUpJoyGameAccount createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			//反序列化
		String userId=source.readString();
		String cpId=source.readString();
		String loginName=source.readString();
		String pwd=source.readString();
		String flag=source.readString();
			
		OneUpJoyGameAccount oneUpJoyGameAccount=new OneUpJoyGameAccount();
		oneUpJoyGameAccount.setCpId(cpId);
		oneUpJoyGameAccount.setFlag(flag);
		oneUpJoyGameAccount.setLoginName(loginName);
		oneUpJoyGameAccount.setPwd(pwd);
		oneUpJoyGameAccount.setUserId(userId);
		
			return oneUpJoyGameAccount;
		}

		@Override
		public OneUpJoyGameAccount[] newArray(int size) {
			// TODO Auto-generated method stub
			return new OneUpJoyGameAccount[size];
		}
	};
 
}
