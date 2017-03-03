/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /home/a/svn/C_BLL01_T828_Laser/C_BLL01_T828_Laser/TRUNK/CODE/android/device/customer/BLL_Laser/apps/SciflyLauncher/src/com/cqsmiletv/game/RemoteOneUpJoyGameAccount.aidl
 */
package com.cqsmiletv.game;
public interface RemoteOneUpJoyGameAccount extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.cqsmiletv.game.RemoteOneUpJoyGameAccount
{
private static final java.lang.String DESCRIPTOR = "com.cqsmiletv.game.RemoteOneUpJoyGameAccount";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.cqsmiletv.game.RemoteOneUpJoyGameAccount interface,
 * generating a proxy if needed.
 */
public static com.cqsmiletv.game.RemoteOneUpJoyGameAccount asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.cqsmiletv.game.RemoteOneUpJoyGameAccount))) {
return ((com.cqsmiletv.game.RemoteOneUpJoyGameAccount)iin);
}
return new com.cqsmiletv.game.RemoteOneUpJoyGameAccount.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_getOneUpJoyGameAccount:
{
data.enforceInterface(DESCRIPTOR);
com.cqsmiletv.game.OneUpJoyGameAccount _result = this.getOneUpJoyGameAccount();
reply.writeNoException();
if ((_result!=null)) {
reply.writeInt(1);
_result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.cqsmiletv.game.RemoteOneUpJoyGameAccount
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public com.cqsmiletv.game.OneUpJoyGameAccount getOneUpJoyGameAccount() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
com.cqsmiletv.game.OneUpJoyGameAccount _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getOneUpJoyGameAccount, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
_result = com.cqsmiletv.game.OneUpJoyGameAccount.CREATOR.createFromParcel(_reply);
}
else {
_result = null;
}
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_getOneUpJoyGameAccount = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public com.cqsmiletv.game.OneUpJoyGameAccount getOneUpJoyGameAccount() throws android.os.RemoteException;
}
