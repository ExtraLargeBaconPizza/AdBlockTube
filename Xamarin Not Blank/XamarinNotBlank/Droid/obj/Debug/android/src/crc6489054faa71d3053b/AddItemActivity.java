package crc6489054faa71d3053b;


public class AddItemActivity
	extends android.app.Activity
	implements
		mono.android.IGCUserPeer
{
/** @hide */
	public static final String __md_methods;
	static {
		__md_methods = 
			"n_onCreate:(Landroid/os/Bundle;)V:GetOnCreate_Landroid_os_Bundle_Handler\n" +
			"";
		mono.android.Runtime.register ("XamarinNotBlank.Droid.AddItemActivity, XamarinNotBlank.Droid", AddItemActivity.class, __md_methods);
	}


	public AddItemActivity ()
	{
		super ();
		if (getClass () == AddItemActivity.class)
			mono.android.TypeManager.Activate ("XamarinNotBlank.Droid.AddItemActivity, XamarinNotBlank.Droid", "", this, new java.lang.Object[] {  });
	}


	public void onCreate (android.os.Bundle p0)
	{
		n_onCreate (p0);
	}

	private native void n_onCreate (android.os.Bundle p0);

	private java.util.ArrayList refList;
	public void monodroidAddReference (java.lang.Object obj)
	{
		if (refList == null)
			refList = new java.util.ArrayList ();
		refList.add (obj);
	}

	public void monodroidClearReferences ()
	{
		if (refList != null)
			refList.clear ();
	}
}