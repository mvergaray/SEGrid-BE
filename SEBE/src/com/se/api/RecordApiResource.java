package com.se.api;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import javax.ws.rs.core.MediaType;

import com.se.bl.BussinesLogic;
import com.se.errorhandling.AppException;
import com.se.errorhandling.ErrorMessage;
import com.se.responses.RecordsResponse;
import com.se.structures.Record;
import com.se.util.PropertyValues;
import com.se.util.ReadWriteExcel;
import com.sun.jersey.multipart.FormDataParam;


@Path("/records")
public class RecordApiResource {
	
	@POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
    public Response uploadExcelFile(@FormDataParam("uploadFile") InputStream fileInputStream) {
		int resultado = 0;
		ReadWriteExcel readFile;
		List<Record> records = new ArrayList<Record>();

        try {
        	readFile = new ReadWriteExcel();System.out.println("escribiendo excel");System.out.println(new Date());
        	readFile.writeExcel(fileInputStream);System.out.println("leyendo excel");System.out.println(new Date());;
        	records = readFile.readExcel();System.out.println("guardando excel");System.out.println(new Date());
        	resultado = BussinesLogic.saveRecords(records);System.out.println("guardado");System.out.println(new Date());
        } catch (AppException e) {
			e.printStackTrace();
			return Response.status(e.getStatus())
				.entity(new ErrorMessage(e))
				.type(MediaType.APPLICATION_JSON).
				build();
		}
        
        return Response.ok(resultado).build();
    }
	
	@GET
	@Path("/list")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response listRecords(@QueryParam("pageStart") int pageStart,
			@QueryParam("pageCount") int pageCount,
			@QueryParam("orderBy") String orderBy,
			@QueryParam("code") String code,
			@QueryParam("document") String document,
			@QueryParam("destination") String destination,
			@QueryParam("address") String address,
			@QueryParam("district") String district,
			@QueryParam("sender") String sender, 
			@QueryParam("startDate") long startDate,
			@QueryParam("endDate") long endDate) {
		RecordsResponse result;
		Record filter = new Record();
		filter.setCode(code);
		filter.setDocument(document);
		filter.setDestination(destination);
		filter.setAddress(address);
		filter.setDistrict(district);
		filter.setSender(sender);
		
		try {
			result = BussinesLogic.listRecords(pageStart, pageCount, orderBy, startDate, endDate, filter);
		} catch (AppException e) {
			return Response.status(e.getStatus())
					.entity(new ErrorMessage(e))
					.type(MediaType.APPLICATION_JSON).
					build();
		}
		
		return Response.ok(result).build();
	}
	
	@GET
	@Path("getFilesName")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
    public Response getFilesName(@QueryParam("code")String code) {
		List<String> result = new ArrayList<String>();
		ReadWriteExcel readFile;
		
		try {
			readFile = new ReadWriteExcel();
			result = readFile.getFilesName(code);
		} catch (AppException e) {
			e.printStackTrace();
			return Response.status(e.getStatus())
					.entity(new ErrorMessage(e))
					.type(MediaType.APPLICATION_JSON).
					build();
		}
		
        return Response
        		.ok(result)
        		.build();
    }
	
	@GET
	@Path("download")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/pdf")
    public Response downloadExcelFile(@QueryParam("code")String code) {
		File result;
		ReadWriteExcel readFile;
		
		try {
			readFile = new ReadWriteExcel();
			result = readFile.getFile(code);
		} catch (AppException e) {
			e.printStackTrace();
			return Response.status(e.getStatus())
					.entity(new ErrorMessage(e))
					.type(MediaType.APPLICATION_JSON).
					build();
		}
		
        return Response
        		.ok((Object) result)
        		.header("Content-Disposition", "attachment; filename=\"" + code + "\"")
        		.build();
    }
	
	@PUT
	@Path("/update")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateRecord(Record record) {
		RecordsResponse result = new RecordsResponse();
		try {
			result = BussinesLogic.updateRecord(record);
		} catch (AppException e) {
			e.printStackTrace();
			return Response.status(e.getStatus())
				.entity(new ErrorMessage(e))
				.type(MediaType.APPLICATION_JSON).
				build();
		}
		return Response.ok(result).build();
	}
	

	@GET
	@Path("creation_code_list")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCreationCodeList() {
		List<String> result = new ArrayList<String>();
		
		try {
			result = BussinesLogic.getCreationCodeList();
		} catch (AppException e) {
			e.printStackTrace();
			return Response.status(e.getStatus())
				.entity(new ErrorMessage(e))
				.type(MediaType.APPLICATION_JSON).
				build();
		}
		return Response.ok(result).build();
	}
	
	@DELETE
	@Path("delete")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteRecordsByCreationCode(@QueryParam("creationCode")String creationCode) {
		int result;
		
		try {
			result = BussinesLogic.deleteRecordsByCreationCode(creationCode);
		} catch (AppException e) {
			e.printStackTrace();
			return Response.status(e.getStatus())
				.entity(new ErrorMessage(e))
				.type(MediaType.APPLICATION_JSON).
				build();
		}
		return Response.ok(result).build();
	}
	
	
	@GET
	@Path("/getKey")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response test(@QueryParam("key")String key) {
		String result = "";
		try {
			result = (new PropertyValues()).getPropertyValues(key);
		} catch (AppException e) {
			e.printStackTrace();
			return Response.status(e.getStatus())
					.entity(new ErrorMessage(e))
					.type(MediaType.APPLICATION_JSON).
					build();
		}
		
		return Response.ok(result).build();
	}
}
