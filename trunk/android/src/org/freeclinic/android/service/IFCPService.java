/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: src\\org\\freeclinic\\android\\service\\IFCPService.aidl
 */
package org.freeclinic.android.service;
import android.os.IBinder;

public interface IFCPService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements org.freeclinic.android.service.IFCPService
{
private static final java.lang.String DESCRIPTOR = "org.freeclinic.android.service.IFCPService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an IFCPService interface,
 * generating a proxy if needed.
 */
public static org.freeclinic.android.service.IFCPService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
org.freeclinic.android.service.IFCPService in = (org.freeclinic.android.service.IFCPService)obj.queryLocalInterface(DESCRIPTOR);
if ((in!=null)) {
return in;
}
return new org.freeclinic.android.service.IFCPService.Stub.Proxy(obj);
}
public android.os.IBinder asBinder()
{
return this;
}
public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags)
{
try {
switch (code)
{
case TRANSACTION_login:
{
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
boolean _result = this.login(_arg0, _arg1);
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_getPatientList:
{
java.lang.String[] _arg0;
int _arg0_length = data.readInt();
if ((_arg0_length<0)) {
_arg0 = null;
}
else {
_arg0 = new java.lang.String[_arg0_length];
}
int[] _arg1;
int _arg1_length = data.readInt();
if ((_arg1_length<0)) {
_arg1 = null;
}
else {
_arg1 = new int[_arg1_length];
}
int _result = this.getPatientList(_arg0, _arg1);
reply.writeInt(_result);
reply.writeStringArray(_arg0);
reply.writeIntArray(_arg1);
return true;
}
case TRANSACTION_getPatient:
{
int _arg0;
_arg0 = data.readInt();
boolean _result = this.getPatient(_arg0);
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_storePatient:
{
int _arg0;
_arg0 = data.readInt();
boolean _result = this.storePatient(_arg0);
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_isKilled:
{
boolean _result = this.isKilled();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_kill:
{
this.kill();
return true;
}
}
}
catch (android.os.DeadObjectException e) {
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements org.freeclinic.android.service.IFCPService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
public android.os.IBinder asBinder()
{
return mRemote;
}
public boolean login(java.lang.String username, java.lang.String password) throws android.os.DeadObjectException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeString(username);
_data.writeString(password);
mRemote.transact(Stub.TRANSACTION_login, _data, _reply, 0);
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public int getPatientList(java.lang.String[] patientList, int[] idList) throws android.os.DeadObjectException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
if ((patientList==null)) {
_data.writeInt(-1);
}
else {
_data.writeInt(patientList.length);
}
if ((idList==null)) {
_data.writeInt(-1);
}
else {
_data.writeInt(idList.length);
}
mRemote.transact(Stub.TRANSACTION_getPatientList, _data, _reply, 0);
_result = _reply.readInt();
_reply.readStringArray(patientList);
_reply.readIntArray(idList);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public boolean getPatient(int patientID) throws android.os.DeadObjectException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInt(patientID);
mRemote.transact(Stub.TRANSACTION_getPatient, _data, _reply, 0);
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public boolean storePatient(int patientID) throws android.os.DeadObjectException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInt(patientID);
mRemote.transact(Stub.TRANSACTION_storePatient, _data, _reply, 0);
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public boolean isKilled() throws android.os.DeadObjectException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
mRemote.transact(Stub.TRANSACTION_isKilled, _data, _reply, 0);
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public void kill() throws android.os.DeadObjectException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
mRemote.transact(Stub.TRANSACTION_kill, _data, null, 0);
}
finally {
_data.recycle();
}
}
}
static final int TRANSACTION_login = (IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_getPatientList = (IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_getPatient = (IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_storePatient = (IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_isKilled = (IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_kill = (IBinder.FIRST_CALL_TRANSACTION + 5);
}
public boolean login(java.lang.String username, java.lang.String password) throws android.os.DeadObjectException;
public int getPatientList(java.lang.String[] patientList, int[] idList) throws android.os.DeadObjectException;
public boolean getPatient(int patientID) throws android.os.DeadObjectException;
public boolean storePatient(int patientID) throws android.os.DeadObjectException;
public boolean isKilled() throws android.os.DeadObjectException;
public void kill() throws android.os.DeadObjectException;
}
