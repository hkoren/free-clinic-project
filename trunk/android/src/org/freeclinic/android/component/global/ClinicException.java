package org.freeclinic.android.component.global;

import android.content.Context;
import android.util.Log;

public class ClinicException extends org.freeclinic.common.network.exception.ClinicException {
	public static final long serialVersionUID=1;
	private Context context;
	
	public ClinicException(Context context, String loc) {
		super(loc);
		Log.e(loc, this.toString());
	}
}
