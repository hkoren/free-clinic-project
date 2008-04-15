package org.freeclinic.common.type;

import java.util.Iterator;

import org.freeclinic.common.network.exception.InvalidClinicVariableException;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class ClinicArrayRequest<T extends ClinicInfo> extends ClinicRequest {

protected static final String ARRAY = "array";
	
	protected T[] m_itemArray;
	
	public ClinicArrayRequest() {
		super();
	}

	public ClinicArrayRequest(int requestCode) {
		super(requestCode);
	}
	
	public ClinicArrayRequest(int requestCode, T[] itemArray) {
		this(requestCode);
		m_itemArray = itemArray;
	}
	
	public ClinicArrayRequest(JSONObject json) throws JSONException {
		super(json);
	}
	
	public abstract void unmarshallArray(JSONObject json) throws JSONException;
	
	@Override
	public void unmarshallJSON(JSONObject json) throws JSONException {
		unmarshallArray(json);
		Iterator keyItr = json.keys();
		Object currId;
		Integer dataType;
		while(keyItr.hasNext()) {
			currId = keyItr.next();
			dataType = m_fieldTypeMap.get(currId);
			if(currId.equals(ARRAY) || currId.toString().startsWith(ARRAY + "@_"))
				continue;
			switch(dataType) {
				case BOOLEAN_FIELD:
					setBoolean(currId.toString(), new Boolean(json.getBoolean(currId.toString())));
					break;
				case DOUBLE_FIELD:
					setDouble(currId.toString(), new Double(json.getDouble(currId.toString())));
					break;
				case INTEGER_FIELD:
					setInteger(currId.toString(), new Integer(json.getInt(currId.toString())));
					break;
				case LONG_FIELD:
					setLong(currId.toString(), new Long(json.getLong(currId.toString())));
					break;
				case STRING_FIELD:
					setString(currId.toString(), new String(json.getString(currId.toString())));
					break;
				default:
					throw new InvalidClinicVariableException(currId.toString());
			}
		}
	}
	
	@Override
	public JSONObject JSON() throws JSONException {
		JSONObject output = super.JSON();
		output.put(ARRAY, m_itemArray.length);
		for(int i = 0; i < m_itemArray.length; i++)
			output.put(ARRAY + "@_" + i, m_itemArray[i].JSON());		
		return output;
	}
	
	public T[] getArray() {
		return m_itemArray;
	}
	public T first() {
		return m_itemArray[0];
	}
	
	/**
	 * Returns a json object for array object at index
	 * @param json all your info
	 * @param i the array index you want
	 * @return
	 */
	protected JSONObject getArrayJSON(JSONObject json, int i) throws JSONException
	{
		return json.getJSONObject(ARRAY + "@_" + i);
	}

}
