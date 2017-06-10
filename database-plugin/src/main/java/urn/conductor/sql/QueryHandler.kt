package urn.conductor.sql

import jdk.nashorn.api.scripting.ScriptObjectMirror
import org.apache.logging.log4j.LogManager
import urn.conductor.ElementHandler
import urn.conductor.Engine
import urn.conductor.database.xml.Query
import java.sql.Connection
import java.sql.ResultSet
import java.sql.Types

class QueryHandler : ElementHandler<Query> {
	override val handles: Class<Query>
		get() = Query::class.java

	private val logger = LogManager.getLogger()

	override fun process(element: Query, engine: Engine, processChild: (Any) -> Unit) {
		val connection = engine.get(element.connectionRef) as Connection
		val query = element.value.trim()

		logger.info("Executing query [$query]")

		val result = connection.prepareStatement(query).executeQuery()
		val resultTable = ArrayList<ArrayList<Any?>>()

		val meta = result.metaData
		val headers = ArrayList<String>()
		val dataTypesToString = ArrayList<(ResultSet) -> Any?>()
		val maxColumnSize = ArrayList<Int>()

		for (colIndex in 0..meta.columnCount - 1) {
			val jdbcIndex = colIndex + 1
			meta.getColumnLabel(jdbcIndex).let {
				headers.add(it.toLowerCase())
				maxColumnSize.add(it.length)
			}

			fun getConverter(type: Int, i: Int): (ResultSet) -> Any? {
				val readerMethod: ResultSet.(Int) -> Any? = when (type) {
					Types.BIT -> ResultSet::getBoolean
					Types.TINYINT -> ResultSet::getByte
					Types.SMALLINT -> ResultSet::getShort
					Types.INTEGER -> ResultSet::getInt
					Types.BIGINT -> ResultSet::getLong
					Types.FLOAT -> ResultSet::getFloat
					Types.REAL -> ResultSet::getDouble
					Types.DOUBLE -> ResultSet::getDouble
					Types.NUMERIC -> ResultSet::getDouble
					Types.DECIMAL -> ResultSet::getDouble
					Types.CHAR -> ResultSet::getString
					Types.VARCHAR -> ResultSet::getString
					Types.LONGVARCHAR -> ResultSet::getString
					Types.DATE -> ResultSet::getDate
					Types.TIME -> ResultSet::getTime
					Types.TIMESTAMP -> ResultSet::getTimestamp
					Types.BINARY -> ResultSet::getBinaryStream
					Types.VARBINARY -> ResultSet::getBinaryStream
					Types.LONGVARBINARY -> ResultSet::getBinaryStream
					Types.ARRAY -> ResultSet::getArray
					Types.BLOB -> ResultSet::getBlob
					Types.CLOB -> ResultSet::getClob
					Types.REF -> ResultSet::getRef
					Types.BOOLEAN -> ResultSet::getBoolean
					Types.ROWID -> ResultSet::getRowId
					Types.NCHAR -> ResultSet::getNString
					Types.NVARCHAR -> ResultSet::getNString
					Types.LONGNVARCHAR -> ResultSet::getNString
					Types.NCLOB -> ResultSet::getNClob
					Types.NULL -> return { null }
					Types.OTHER -> return { "<other>" }
					Types.JAVA_OBJECT -> ResultSet::getObject
					Types.DISTINCT -> return { "<distinct>" }
					Types.STRUCT -> return { "<struct>" }
					Types.DATALINK -> return { "<datalink>" }
					Types.SQLXML -> return { it.getSQLXML(i).toString() }
					Types.REF_CURSOR -> return { "<ref_cursor>" }
					Types.TIME_WITH_TIMEZONE -> ResultSet::getTime
					Types.TIMESTAMP_WITH_TIMEZONE -> ResultSet::getTimestamp
					else -> ResultSet::getString
				}
				return { it.readerMethod(i) }
			}

			dataTypesToString.add(getConverter(meta.getColumnType(jdbcIndex), jdbcIndex))
		}

		while (result.next()) {
			val row = ArrayList<Any?>()
			for ((index, converter) in dataTypesToString.withIndex()) {
				converter(result).let {
					row.add(it)
					if (maxColumnSize.get(index) < it.toString().length) {
						maxColumnSize.set(index, it.toString().length)
					}
				}
			}
			resultTable.add(row)
		}

		val separator = maxColumnSize.map { "-".repeat(it + 2) }.joinToString(separator = "-", prefix = "-", postfix = "-")

		logger.info(separator)
		logger.info(headers
				.withIndex()
				.map { it.value.padEnd(maxColumnSize.get(it.index)) }
				.map { " $it " }
				.joinToString(separator = "|", prefix = "|", postfix = "|"))
		logger.info(separator)

		resultTable.forEach {
			logger.info(it
					.withIndex()
					.map { it.value.toString().padEnd(maxColumnSize.get(it.index)) }
					.map { " $it " }
					.joinToString(separator = "|", prefix = "|", postfix = "|"))
		}

		logger.info(separator)

		if (element.id != null) {
			ArrayList<HashMap<String, Any?>>().apply {
				resultTable.withIndex().forEach {
					this.add(HashMap<String, Any?>().apply {
						it.value.withIndex().forEach {
							this[headers[it.index]] = it.value
						}
					})
				}
			}.let {
				engine.put(element.id, it)
			}
		}
	}

}