package org.netf.evidb.dump.batch.dao;

import java.util.List;
import java.util.Map;

import org.seasar.doma.Dao;
import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.builder.SelectBuilder;

@Dao
@ConfigAutowireable
public interface GenericDao {

	default List<Map<String, Object>> selectAll(String query) {
		Config config = Config.get(this);
		SelectBuilder builder = SelectBuilder.newInstance(config);
		builder.sql(query);
		return builder.getMapResultList(MapKeyNamingType.NONE);
	};

}
