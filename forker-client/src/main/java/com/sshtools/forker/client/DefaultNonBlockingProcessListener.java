package com.sshtools.forker.client;

import java.nio.ByteBuffer;

import com.sshtools.forker.client.impl.nonblocking.NonBlockingProcess;

/**
 * Default implementation of a {@link NonBlockingProcessListener}.
 */
public class DefaultNonBlockingProcessListener implements NonBlockingProcessListener {
	@Override
	public void onError(Exception exception, NonBlockingProcess process, boolean existing) {
	}

	@Override
	public void onExit(int exitCode, NonBlockingProcess process) {
	}

	@Override
	public void onStdout(NonBlockingProcess process, ByteBuffer buffer, boolean closed) {
	}

	@Override
	public void onStderr(NonBlockingProcess process, ByteBuffer buffer, boolean closed) {
	}

	@Override
	public boolean onStdinReady(NonBlockingProcess process, ByteBuffer buffer) {
		return false;
	}
}