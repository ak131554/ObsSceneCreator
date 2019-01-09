package com.wmi.windows;

public class AudioDevice
{
	public static final AudioDevice DEFAULT_INPUT = new AudioDevice("Standard", "default")
	{
		@Override
		public boolean isAudioInputDevice()
		{
			return true;
		}
	};
	public static final AudioDevice DEFAULT_OUTPUT = new AudioDevice("Standard", "default")
	{
		@Override
		public boolean isAudioInputDevice()
		{
			return false;
		}
	};

	private final String name;
	private final String deviceId;

	public AudioDevice(final String name, final String deviceId)
	{
		this.name = name;
		this.deviceId = deviceId;
	}

	public String getName()
	{
		return name;
	}

	public String getDeviceId()
	{
		return deviceId;
	}

	public boolean isAudioInputDevice()
	{
		return deviceId.startsWith("{0.0.1.");
	}

	@Override
	public String toString()
	{
		return getName();
	}
}
