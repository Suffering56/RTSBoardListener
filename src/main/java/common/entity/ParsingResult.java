package common.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ParsingResult {

	public void add(String name, String field, String value) {
		resultList.add(new SenderEntity(name, field, value));
	}
	
	public void addInstrument(String name) {
		instrumentSet.add(name);
	}

	public SenderEntity get(int index) {
		return resultList.get(index);
	}

	public void clear() {
		resultList.clear();
		instrumentSet.clear();
	}

	public int size() {
		return resultList.size();
	}

	public boolean hasResult() {
		return size() > 0;
	}

	public Set<String> getInstrumentSet() {
		return instrumentSet;
	}

	public List<SenderEntity> getResultList() {
		return resultList;
	}

	private List<SenderEntity> resultList = new ArrayList<>();
	private Set<String> instrumentSet = new HashSet<>();
}
