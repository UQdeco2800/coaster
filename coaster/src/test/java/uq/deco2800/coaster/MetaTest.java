package uq.deco2800.coaster;

import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ryanj on 18/09/2016.
 */
public class MetaTest {
	private static final Logger logger = LoggerFactory.getLogger(MetaTest.class);
	private final List<String> ValidLoadLines = Arrays.asList("uq.deco2800.coaster.TestHelper.loadWithFXML();", "uq.deco2800.coaster.TestHelper.load();",
			"init();", "initialise();");
	private boolean isWin = System.getProperty("os.name").toLowerCase().contains("win");
	private Map<String, Integer> blameMap = new HashMap<>();


	@Before

	public void init() {
		TestHelper.load();
		try {
			String[] cmd = {"git", "version"};
			if (isWin) {
				cmd[0] = "git.exe";
			}
			Runtime.getRuntime().exec(cmd).waitFor();
		} catch (Exception e) {
		}
	}

//	@Test
//	public void loadingTest() {
//		boolean broken = false;
//		File dir = new File("src/test/java");
//		for (File f : dir.listFiles()) {
//			String prev = "";
//			String line = "";
//			boolean nextLineIsLoad = false;
//			try {
//				BufferedReader br = new BufferedReader(new FileReader(f));
//
//				while (null != line && !line.contains("public class")) {
//					line = br.readLine();
//				}
//				while (null != (line = br.readLine())) {
//					line = line.trim();
//					if (line.equals("") || line.startsWith("//")) {
//						continue;
//					}
//					if (nextLineIsLoad) {
//						nextLineIsLoad = false;
//						if (line.length() > 0) {
//							if (valid(line)) {
//								continue;
//							} else {
//								logger.error(f.getName() + ":" + prev.split(" ")[2] +
//										"\thas @Test method without a load on first line.");
//								broken = true;
//							}
//						}
//					}
//					if (line.contains("public void") && prev.contains("@Before")) {
//						break;
//					}
//					if (prev.contains("@Test")) {
//						if (line.contains("public void")) {
//							nextLineIsLoad = true;
//						} else {
//							logger.error(f.getName() + "\thas @Test without immediate method");
//							broken = true;
//							break;
//						}
//					}
//					prev = line;
//				}
//				br.close();
//			} catch (Exception e) {
//				logger.debug("failure reading source files", e);
//			}
//		}
//		assert !broken;
//	}
//
//	private boolean valid(String s) {
//		for (String l : ValidLoadLines) {
//			if (s.equals(l)) {
//				return true;
//			}
//		}
//		return false;
//	}
//
//	@Test
//	public void piedPiperTest() {
//		richardIsAngry = false;
//		try {
//			Files.walk(Paths.get(srcPath)).filter(Files::isRegularFile).forEach(f -> piedPiper(f.toFile()));
//		} catch (Exception e) {
//			logger.error("failure walking source files", e);
//		}
//		blameMap.forEach((name, i) -> blame(name, i));
//		assert !richardIsAngry;
//	}
//
//
//	private void piedPiper(File f) {
//		if (!f.getName().endsWith(".java")) {
//			return;
//		}
//		String line;
//		int i = 1;
//		try {
//			BufferedReader br = new BufferedReader(new FileReader(f));
//			while (null != (line = br.readLine())) {
//				if (line.startsWith("    ")) {
//					log(f, i);
//					richardIsAngry = true;
//				}
//				i++;
//			}
//			br.close();
//		} catch (Exception e) {
//			logger.error("failure reading source files", e);
//		}
//	}
//
//
//	private void log(File f, int i) {
//		if (hasGit) {
//			try {
//				String[] cmd = {"git", "blame", f.getAbsolutePath(), "-L", i + "," + i};
//				if (isWin) {
//					cmd[0] = "git.exe";
//				}
//				//I know doing line by line is slow but I can't be bothered fixing it cause this should never
//				//be called because we are all using tabs not spaces right guys?
//				Process p = Runtime.getRuntime().exec(cmd);
//				BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
//				StringBuilder builder = new StringBuilder();
//				String l = null;
//				while ((l = reader.readLine()) != null) {
//					builder.append(l);
//				}
//				p.waitFor();
//				String blameName = builder.toString().split("\\(")[1].split("2016")[0];
//				logger.info(f.getPath().split("deco2800")[1] + ":" + i + "\tTabs not spaces\tblame: " + blameName);
//				//logger.info(f.getPath().split("deco2800")[1] + ":" + i + "\tTabs not spaces\tblame:" + blameName);
//				Integer blames = blameMap.get(blameName);
//				if (blames == null) {
//					blameMap.put(blameName, 1);
//				} else {
//					blameMap.put(blameName, blames + 1);
//				}
//				reader.close();
//				return;
//			} catch (Exception e) {
//				logger.error("Failed when git was used", e);
//			}
//
//		}
//		logger.info(f.getPath().split("deco2800")[1] + ":" + i + "\tTabs not spaces");
//
//	}
//
//	private void blame(String n, int i) {
//		logger.info(n + "blamed for " + i + " lines.");
//	}
}
