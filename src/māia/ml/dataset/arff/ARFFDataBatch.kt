package māia.ml.dataset.arff

import māia.ml.dataset.DataBatch
import māia.ml.dataset.DataColumn
import māia.ml.dataset.headers.MutableDataColumnHeaders
import māia.ml.dataset.type.DataRepresentation
import māia.ml.dataset.view.readOnlyViewColumn
import māia.util.toArray

/**
 * The type of data-batch returned by the ARFF loader.
 *
 * @param relationName
 *          The name of the ARFF relation.
 * @param headersInternal
 *          The ARFF attribute headers.
 * @param rows
 *          The ARFF data-rows.
 *
 * @author Corey Sterling (csterlin at waikato dot ac dot nz)
 */
class ARFFDataBatch internal constructor(
    relationName: String,
    private val headersInternal : MutableDataColumnHeaders,
    rows: Iterator<ARFFDataRow>
): ARFFDataStream(relationName, headersInternal, rows), DataBatch<ARFFDataRow> {
    /** Immediately consumes the row iterator into a batch. */
    private val batchedRows = rows.toArray()

    override fun <T> getColumn(
        representation : DataRepresentation<*, *, T>
    ) : DataColumn<T> = readOnlyViewColumn(representation)

    override fun <T> getValue(
        representation : DataRepresentation<*, *, out T>,
        rowIndex : Int
    ) : T {
        // No need to check for ownership of the representation as the row
        // will do that itself
        return batchedRows[rowIndex].getValue(representation)
    }

    override val numRows : Int = batchedRows.size

    override fun getRow(rowIndex : Int) : ARFFDataRow = batchedRows[rowIndex]

    override fun rowIterator() : Iterator<ARFFDataRow> = batchedRows.iterator()

}
