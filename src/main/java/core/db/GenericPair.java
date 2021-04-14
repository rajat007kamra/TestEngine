package core.db;

public class GenericPair<K, V> 
{
	private K dataDescriptor;
	private V dataValue;

	public GenericPair() {

	}

	public GenericPair(K dataDescriptor, V dataValue) {
		this.dataDescriptor = dataDescriptor;
		this.dataValue = dataValue;
	}

	public K getDataDescriptor() {
		return dataDescriptor;
	}

	public void setDataDescriptor(K dataDescriptor) {
		this.dataDescriptor = dataDescriptor;
	}

	public V getDataValue() {
		return dataValue;
	}

	public void setDataValue(V dataValue) {
		this.dataValue = dataValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataDescriptor == null) ? 0 : dataDescriptor.hashCode());
		result = prime * result + ((dataValue == null) ? 0 : dataValue.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		GenericPair<K, V> other = (GenericPair<K, V>) obj;
		if (dataDescriptor == null) {
			if (other.dataDescriptor != null) {
				return false;
			}
		} else if (!dataDescriptor.equals(other.dataDescriptor)) {
			return false;
		}
		if (dataValue == null) {
			if (other.dataValue != null) {
				return false;
			}
		} else if (!dataValue.equals(other.dataValue)) {
			return false;
		}
		return true;
	}

}
