import java.util.HashMap;
import java.util.Map;

import com.hlb.base.monitor.model.LogCfg;

public class TestReflex {
	
	
	public static void main(String[] args) {
		
		String regx = "map.get(ioio).getContext()";
		LogCfg cfg = new LogCfg();
		cfg.setContext("AAA");
		Map<String, LogCfg> map = new HashMap<String, LogCfg>();
		map.put("ioio", cfg);
	}
	
}
