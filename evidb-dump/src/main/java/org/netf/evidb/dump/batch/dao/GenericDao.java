package org.netf.evidb.dump.batch.dao;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import org.seasar.doma.Dao;
import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.builder.SelectBuilder;

@Dao
@ConfigAutowireable
public interface GenericDao {

	default <R> R selectAll(String query,
			Function<Stream<Map<String, Object>>, R> mapper) {

		Config config = Config.get(this);

		SelectBuilder builder = SelectBuilder.newInstance(config);

		return builder.sql(query).streamMap(MapKeyNamingType.NONE, mapper);

	};

}
