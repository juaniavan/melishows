package ar.com.juani.melishows.dao.specification;

public enum ShowOrderBy {

	SHOW {
		@Override
		public String getShowAlias() {
			return "name";
		}

		@Override
		public String getShowingAlias() {
			return "show.name";
		}
	},
	LOCATION {
		@Override
		public String getShowAlias() {
			return "theater.location.city";
		}

		@Override
		public String getShowingAlias() {
			return "show.theater.location.city";
		}
	},
	THEATER {
		@Override
		public String getShowAlias() {
			return "theater.name";
		}

		@Override
		public String getShowingAlias() {
			return "show.theater.name";
		}
	},
	ARTIST {
		@Override
		public String getShowAlias() {
			return "artist";
		}

		@Override
		public String getShowingAlias() {
			return "show.artist";
		}
	};

	public abstract String getShowAlias();
	public abstract String getShowingAlias();
}