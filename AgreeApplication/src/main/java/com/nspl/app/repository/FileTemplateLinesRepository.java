package com.nspl.app.repository;

import com.nspl.app.domain.DataViewsColumns;
import com.nspl.app.domain.FileTemplateLines;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the FileTemplateLines entity.
 */
@SuppressWarnings("unused")
public interface FileTemplateLinesRepository extends JpaRepository<FileTemplateLines,Long> {

	List<FileTemplateLines> findByTemplateId(Long templateId);
	List<FileTemplateLines> findByTemplateIdOrderByLineNumber(Long templateId);
	
	@Query(value="SELECT * FROM t_file_template_lines where template_id = (select  id from t_file_templates where id_for_display =:idForDisplay ) ", nativeQuery=true)
	List<FileTemplateLines> findBytempIdForDisplay(@Param("idForDisplay") String idForDisplay);
	
	//Kiran
	List<FileTemplateLines> findByTemplateIdAndRecordTYpe(Long templateId, String string);
	
	List<FileTemplateLines> findByTemplateIdIn(List<Long> templateId);
	
	FileTemplateLines findByConstantValue(String rowIdentifier);
//	FileTemplateLines findByRecordTYpeAndTemplateId(String string,Long templateId);
	
	List<FileTemplateLines> findByTemplateIdAndConstantValue(Long templateId,String recordType);
	List<FileTemplateLines> findByTemplateIdAndConstantValueAndRecordTYpe(Long templateId, String recordType, String string);
	
	FileTemplateLines findByTemplateIdAndMasterTableReferenceColumn(Long templateId, String masterCol);
	
	FileTemplateLines findByTemplateIdAndColumnHeader(Long templateId, String colHeader);
	
	FileTemplateLines findByTemplateIdAndColumnAlias(Long templateId, String colHeader);
	
	@Query(value="SELECT * FROM t_file_template_lines where template_id in (SELECT id FROM t_file_templates where tenant_id =:tenantId);", nativeQuery=true)
	List<FileTemplateLines> fetchFileTemplatesByFileTempId(@Param("tenantId") Long tenantId);
	
	@Query(value="select column_alias from t_file_template_lines where id in(:ids)", nativeQuery=true)
	List<String> fetchColumnAliasByIds(@Param("ids") List<Long> ids);
	//kiran
	List<FileTemplateLines> findByConstantValueAndRecordTYpe(String recordType,
			String string);
	List<FileTemplateLines> findByConstantValueAndRecordTYpeAndTemplateId(
			String recordType, String string, Long templateId);
	
	@Query(value="select master_table_reference_column from t_file_template_lines where id = :id", nativeQuery=true)
	String fetchMasterTableRefColById(@Param("id") Long id);
	
	@Query(value="SELECT column_alias FROM t_file_template_lines where id in (SELECT distinct(ref_dv_column) FROM t_data_views_columns where data_view_id = :viewId and qualifier in ('TRANSDATE','CURRENCYCODE'))", nativeQuery=true)
	List<String> fetchViewQualifiers(@Param("viewId") Long viewId);
	
	List<FileTemplateLines> findByIntermediateIdOrderByLineNumber(Long long1);
//	List<FileTemplateLines> findByIntermediateIdOrderByLineNumber(String long1);
	List<FileTemplateLines> findByTemplateIdAndIntermediateIdOrderByLineNumber(Long templateId, Long intermediateId);
	
	List<FileTemplateLines> findByTemplateIdInAndIntermediateIdInOrderByLineNumber(List<Long> templateId, List<Long> intermediateIdList);
	
	List<FileTemplateLines> findByTemplateIdAndIntermediateIdInOrderByLineNumber(Long templateId, List<Long> intermediateIdList);
	
	// @Rk For Summary File Template
	@Query(value="SELECT * FROM t_file_template_lines where id in (select template_line_id from t_summary_file_template_lines where file_template_id=:templateId);", nativeQuery=true)
	List<FileTemplateLines> fetchSummaryTableLinesInfo(@Param("templateId") Long templateId);
	
	FileTemplateLines findByTemplateIdAndIntermediateIdAndColumnHeader(Long templateId,Long intermediateId,String columnHeader);
	FileTemplateLines findByTemplateIdAndIntermediateIdAndColumnHeaderAndRecordIdentifier(Long templateId, Long valueOf, String columnHeader,String recordStartRow);
}
