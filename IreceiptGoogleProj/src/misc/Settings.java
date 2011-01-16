package misc;

import google.proj.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import android.app.Activity;
import android.content.Context;

public class Settings implements java.io.Serializable {

	private boolean deleteOnServer;
	private int daysToStay;
	private double maxuniquly;
	private double maxmonth;
	private Date dateEx;

	public Settings() {
		deleteOnServer = false;
		daysToStay = -1;
		maxmonth = -1; // לימיט בחודש האחרון
		maxuniquly = -1;// לימיט על תחום תאריכים
		dateEx = null; // החל מהתאריך הזה נבדוק
	}

	public boolean getDeleteOnSerever() {
		return deleteOnServer;
	}

	public boolean needToDelete(iReceipt r) {
		if (needToDeleteByDate()) {
			Date d1 = new Date();
			Date d2 = new Date(r.getRdate().getYear() - 1900, r.getRdate()
					.getMonth() - 1, r.getRdate().getDay());
			long l1 = d1.getTime();
			long l2 = d2.getTime();
			if (l1 - l2 < daysToStay * 86400000)
				return false;
			else
				return true;
		} else
			return false;
	}

	private boolean needToDeleteByDate() {
		if (daysToStay == -1)
			return false;
		else
			return true;
	}

	public void setdaysToStay(int days) {
		this.daysToStay = days;
	}

	public void setDeleteOnServer(boolean bool) {
		deleteOnServer = bool;
	}

	public void setMaxMonth(double max) {

		maxmonth = max;
	}

	public void setMaxUniquely(double max) {
		maxuniquly = max;
	}

	public int getdaysToStay() {
		return daysToStay;
	}

	public double getMaxmonth() {
		return maxmonth;
	}

	public double getMaxUniquely() {
		return maxuniquly;
	}

	@SuppressWarnings("unused")
	public Date getDateEx() {
		return dateEx;
	}

	public void setDate(Date date) {
		dateEx = date;
	}

	public static boolean deleteOnServer() {
		return true;
	}
}