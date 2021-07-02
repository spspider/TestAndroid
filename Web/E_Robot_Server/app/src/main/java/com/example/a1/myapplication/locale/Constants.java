package com.example.a1.myapplication.locale;

/**
 * Class of {@code Intent} constants used by this <i>Locale</i> plug-in.
 */
final class Constants
{
	/**
	 * Private constructor prevents instantiation
	 *
	 * @throws UnsupportedOperationException because this class cannot be instantiated.
	 */
	private Constants()
	{
		throw new UnsupportedOperationException(String.format("%s(): This class is non-instantiable", this.getClass().getSimpleName())); //$NON-NLS-1$
	}

	/**
	 * TYPE: {@code String}
	 * <p>
	 * True means display is on. False means off.
	 */
	protected static final String BUNDLE_EXTRA_SSID = "org.johanhil.ssid.extra.SSID"; //$NON-NLS-1$
}