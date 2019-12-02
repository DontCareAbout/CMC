package us.dontcareabout.cmc.server.chrome;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Preconditions;

public class Agent {
	private static final List<String> argument = Arrays.asList("--headless", "--enable-logging", "--disable-gpu", "--dump-dom");
	private static final Random random = new Random();
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

	private final ArrayList<String> baseCommand = new ArrayList<>();

	/**
	 * 因為是用開 process 的方式，thread 會立刻執行完，並不會佔據 thread pool。
	 * 為了達到「每個 {@link #fetch(String, File)} 之間有間隔時間」的需求，
	 * 所以只能採用 {@link Executors#newSingleThreadExecutor()}，
	 * 另外安排 {@link #queue} 儲存要抓取的參數。
	 */
	private final ScheduledExecutorService es = Executors.newSingleThreadScheduledExecutor();
	private final Queue<Argument> queue = new LinkedList<>();
	private int idleCounter;

	/**
	 * @param exeFolder chrome.exe 的目錄
	 * @param period 每次抓取間隔時間的基準參考值（細節略），單位為秒
	 */
	public Agent(String exeFolder, int period) {
		Preconditions.checkArgument(period > 1);

		baseCommand.add(new File(exeFolder, "chrome.exe").getAbsolutePath());
		baseCommand.addAll(argument);

		es.scheduleAtFixedRate(
			new Runnable(){
				@Override
				public void run() {
					if (queue.isEmpty()) { return; }

					Argument argument = queue.poll();
					ArrayList<String> command = new ArrayList<>(baseCommand);
					command.add(argument.url);
					ProcessBuilder pb = new ProcessBuilder(command);
					pb.redirectOutput(argument.target);

					//超過五次之後有機率 sleep 更長的時間
					boolean idleFlag = idleCounter++ > 5 + random.nextInt(5);

					if (idleFlag) {
						idleCounter = 0;
					}

					try {
						//sleep 的時間必須比 period 大才有效果
						Thread.sleep(
							((idleFlag ? period * 10 : period) + random.nextInt(period * 2)) * 1000L
						);
						//TODO 改 log 機制
						System.out.println("[Chrome Agent] " + dateFormat.format(new Date()) + "\t" + argument.url);
						pb.start();
					} catch (Exception e) {
						System.out.println("[Chrome Agent] " + e.getMessage());
					}
				}
			}, 0, period, TimeUnit.SECONDS
		);
	}

	public void fetch(String url, File target) {
		queue.add(new Argument(url, target));
	}

	private class Argument {
		final String url;
		final File target;

		Argument(String url, File target) {
			this.url = url;
			this.target = target;
		}
	}
}
