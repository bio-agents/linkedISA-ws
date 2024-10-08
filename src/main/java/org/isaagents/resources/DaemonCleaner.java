package org.isaagents.resources;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileDeleteStrategy;

public class DaemonCleaner extends Thread {

	private static ArrayList<String> cleanPaths;

	public static DaemonCleaner INSTANCE = new DaemonCleaner();

	static {
		cleanPaths = new ArrayList<String>();
	}

	@Override
	public void run() {
		while (true) {
			synchronized (cleanPaths) {
				ArrayList<String> toDelete = new ArrayList<String>();
				for (String path : cleanPaths) {
					try {
						FileDeleteStrategy.FORCE.delete(new File(path));
						toDelete.add(path);
					} catch (IOException e) {
					}
				}

				for (String path : toDelete) {
					cleanPaths.remove(path);
				}
			}

			try {
				sleep(300000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void addPath(String path) {
		synchronized (cleanPaths) {
			cleanPaths.add(path);
		}
	}

}
