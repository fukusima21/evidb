package org.netf.evidb.core.constants;

public enum ClassConstants {

	/** */
	StandardDialect("org.netf.evidb.sqlgen.dialect", "StandardDialect"),
	/** */
	HsqldbDialect("org.netf.evidb.sqlgen.dialect", "HsqldbDialect"),
	/** */
	H2Dialect("org.netf.evidb.sqlgen.dialect", "H2Dialect"),
	/** */
	MysqlDialect("org.netf.evidb.sqlgen.dialect", "MysqlDialect"),
	/** */
	PostgresDialect("org.netf.evidb.sqlgen.dialect", "PostgresDialect"),
	/** */
	Db2Dialect("org.netf.evidb.sqlgen.dialect", "Db2Dialect"),
	/** */
	Mssql2008("org.netf.evidb.sqlgen.dialect", "Mssql2008Dialect"),
	/** */
	Mssql("org.netf.evidb.sqlgen.dialect", "MssqlDialect"),
	/** */
	Oracle11Dialect("org.seasar.doma.jdbc.dialect", "Oracle11Dialect"),
	/** */
	OracleDialect("org.netf.evidb.sqlgen.dialect", "OracleDialect"),
	/** */
	bytes("", "byte[]"),;

	private final String packageName;

	private final String simpleName;

	private ClassConstants(String packageName, String simpleName) {
		this.packageName = packageName;
		this.simpleName = simpleName;
	}

	/**
	 * 完全修飾名を返します。
	 *
	 * @return 完全修飾名
	 */
	public String getQualifiedName() {
		return packageName + "." + simpleName;
	}

	/**
	 * パッケージ名を返します。
	 *
	 * @return パッケージ名
	 */
	public String getPackageName() {
		return packageName;
	}

	/**
	 * 単純名を返します。
	 *
	 * @return 単純名
	 */
	public String getSimpleName() {
		return simpleName;
	}

}
