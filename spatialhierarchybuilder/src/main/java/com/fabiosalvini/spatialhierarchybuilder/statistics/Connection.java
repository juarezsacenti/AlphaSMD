package com.fabiosalvini.spatialhierarchybuilder.statistics;


public class Connection {

		private String fromDomain;
		private String toDomain;
		
		public Connection(String fromDomain, String toDomain) {
			this.fromDomain = fromDomain;
			this.toDomain = toDomain;
		}

		public String getFromDomain() {
			return fromDomain;
		}

		public String getToDomain() {
			return toDomain;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((fromDomain == null) ? 0 : fromDomain.hashCode());
			result = prime * result
					+ ((toDomain == null) ? 0 : toDomain.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Connection other = (Connection) obj;
			if (fromDomain == null) {
				if (other.fromDomain != null)
					return false;
			} else if (!fromDomain.equals(other.fromDomain))
				return false;
			if (toDomain == null) {
				if (other.toDomain != null)
					return false;
			} else if (!toDomain.equals(other.toDomain))
				return false;
			return true;
		}
		
}
