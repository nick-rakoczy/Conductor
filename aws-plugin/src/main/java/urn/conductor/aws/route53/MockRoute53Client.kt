package urn.conductor.aws.route53

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.ResponseMetadata
import com.amazonaws.regions.Region
import com.amazonaws.services.route53.AmazonRoute53
import com.amazonaws.services.route53.model.AssociateVPCWithHostedZoneRequest
import com.amazonaws.services.route53.model.AssociateVPCWithHostedZoneResult
import com.amazonaws.services.route53.model.ChangeResourceRecordSetsRequest
import com.amazonaws.services.route53.model.ChangeResourceRecordSetsResult
import com.amazonaws.services.route53.model.ChangeTagsForResourceRequest
import com.amazonaws.services.route53.model.ChangeTagsForResourceResult
import com.amazonaws.services.route53.model.CreateHealthCheckRequest
import com.amazonaws.services.route53.model.CreateHealthCheckResult
import com.amazonaws.services.route53.model.CreateHostedZoneRequest
import com.amazonaws.services.route53.model.CreateHostedZoneResult
import com.amazonaws.services.route53.model.CreateReusableDelegationSetRequest
import com.amazonaws.services.route53.model.CreateReusableDelegationSetResult
import com.amazonaws.services.route53.model.CreateTrafficPolicyInstanceRequest
import com.amazonaws.services.route53.model.CreateTrafficPolicyInstanceResult
import com.amazonaws.services.route53.model.CreateTrafficPolicyRequest
import com.amazonaws.services.route53.model.CreateTrafficPolicyResult
import com.amazonaws.services.route53.model.CreateTrafficPolicyVersionRequest
import com.amazonaws.services.route53.model.CreateTrafficPolicyVersionResult
import com.amazonaws.services.route53.model.CreateVPCAssociationAuthorizationRequest
import com.amazonaws.services.route53.model.CreateVPCAssociationAuthorizationResult
import com.amazonaws.services.route53.model.DeleteHealthCheckRequest
import com.amazonaws.services.route53.model.DeleteHealthCheckResult
import com.amazonaws.services.route53.model.DeleteHostedZoneRequest
import com.amazonaws.services.route53.model.DeleteHostedZoneResult
import com.amazonaws.services.route53.model.DeleteReusableDelegationSetRequest
import com.amazonaws.services.route53.model.DeleteReusableDelegationSetResult
import com.amazonaws.services.route53.model.DeleteTrafficPolicyInstanceRequest
import com.amazonaws.services.route53.model.DeleteTrafficPolicyInstanceResult
import com.amazonaws.services.route53.model.DeleteTrafficPolicyRequest
import com.amazonaws.services.route53.model.DeleteTrafficPolicyResult
import com.amazonaws.services.route53.model.DeleteVPCAssociationAuthorizationRequest
import com.amazonaws.services.route53.model.DeleteVPCAssociationAuthorizationResult
import com.amazonaws.services.route53.model.DisassociateVPCFromHostedZoneRequest
import com.amazonaws.services.route53.model.DisassociateVPCFromHostedZoneResult
import com.amazonaws.services.route53.model.GetChangeRequest
import com.amazonaws.services.route53.model.GetChangeResult
import com.amazonaws.services.route53.model.GetCheckerIpRangesRequest
import com.amazonaws.services.route53.model.GetCheckerIpRangesResult
import com.amazonaws.services.route53.model.GetGeoLocationRequest
import com.amazonaws.services.route53.model.GetGeoLocationResult
import com.amazonaws.services.route53.model.GetHealthCheckCountRequest
import com.amazonaws.services.route53.model.GetHealthCheckCountResult
import com.amazonaws.services.route53.model.GetHealthCheckLastFailureReasonRequest
import com.amazonaws.services.route53.model.GetHealthCheckLastFailureReasonResult
import com.amazonaws.services.route53.model.GetHealthCheckRequest
import com.amazonaws.services.route53.model.GetHealthCheckResult
import com.amazonaws.services.route53.model.GetHealthCheckStatusRequest
import com.amazonaws.services.route53.model.GetHealthCheckStatusResult
import com.amazonaws.services.route53.model.GetHostedZoneCountRequest
import com.amazonaws.services.route53.model.GetHostedZoneCountResult
import com.amazonaws.services.route53.model.GetHostedZoneRequest
import com.amazonaws.services.route53.model.GetHostedZoneResult
import com.amazonaws.services.route53.model.GetReusableDelegationSetRequest
import com.amazonaws.services.route53.model.GetReusableDelegationSetResult
import com.amazonaws.services.route53.model.GetTrafficPolicyInstanceCountRequest
import com.amazonaws.services.route53.model.GetTrafficPolicyInstanceCountResult
import com.amazonaws.services.route53.model.GetTrafficPolicyInstanceRequest
import com.amazonaws.services.route53.model.GetTrafficPolicyInstanceResult
import com.amazonaws.services.route53.model.GetTrafficPolicyRequest
import com.amazonaws.services.route53.model.GetTrafficPolicyResult
import com.amazonaws.services.route53.model.ListGeoLocationsRequest
import com.amazonaws.services.route53.model.ListGeoLocationsResult
import com.amazonaws.services.route53.model.ListHealthChecksRequest
import com.amazonaws.services.route53.model.ListHealthChecksResult
import com.amazonaws.services.route53.model.ListHostedZonesByNameRequest
import com.amazonaws.services.route53.model.ListHostedZonesByNameResult
import com.amazonaws.services.route53.model.ListHostedZonesRequest
import com.amazonaws.services.route53.model.ListHostedZonesResult
import com.amazonaws.services.route53.model.ListResourceRecordSetsRequest
import com.amazonaws.services.route53.model.ListResourceRecordSetsResult
import com.amazonaws.services.route53.model.ListReusableDelegationSetsRequest
import com.amazonaws.services.route53.model.ListReusableDelegationSetsResult
import com.amazonaws.services.route53.model.ListTagsForResourceRequest
import com.amazonaws.services.route53.model.ListTagsForResourceResult
import com.amazonaws.services.route53.model.ListTagsForResourcesRequest
import com.amazonaws.services.route53.model.ListTagsForResourcesResult
import com.amazonaws.services.route53.model.ListTrafficPoliciesRequest
import com.amazonaws.services.route53.model.ListTrafficPoliciesResult
import com.amazonaws.services.route53.model.ListTrafficPolicyInstancesByHostedZoneRequest
import com.amazonaws.services.route53.model.ListTrafficPolicyInstancesByHostedZoneResult
import com.amazonaws.services.route53.model.ListTrafficPolicyInstancesByPolicyRequest
import com.amazonaws.services.route53.model.ListTrafficPolicyInstancesByPolicyResult
import com.amazonaws.services.route53.model.ListTrafficPolicyInstancesRequest
import com.amazonaws.services.route53.model.ListTrafficPolicyInstancesResult
import com.amazonaws.services.route53.model.ListTrafficPolicyVersionsRequest
import com.amazonaws.services.route53.model.ListTrafficPolicyVersionsResult
import com.amazonaws.services.route53.model.ListVPCAssociationAuthorizationsRequest
import com.amazonaws.services.route53.model.ListVPCAssociationAuthorizationsResult
import com.amazonaws.services.route53.model.TestDNSAnswerRequest
import com.amazonaws.services.route53.model.TestDNSAnswerResult
import com.amazonaws.services.route53.model.UpdateHealthCheckRequest
import com.amazonaws.services.route53.model.UpdateHealthCheckResult
import com.amazonaws.services.route53.model.UpdateHostedZoneCommentRequest
import com.amazonaws.services.route53.model.UpdateHostedZoneCommentResult
import com.amazonaws.services.route53.model.UpdateTrafficPolicyCommentRequest
import com.amazonaws.services.route53.model.UpdateTrafficPolicyCommentResult
import com.amazonaws.services.route53.model.UpdateTrafficPolicyInstanceRequest
import com.amazonaws.services.route53.model.UpdateTrafficPolicyInstanceResult
import com.amazonaws.services.route53.waiters.AmazonRoute53Waiters

class MockRoute53Client : AmazonRoute53 {
	override fun getTrafficPolicy(p0: GetTrafficPolicyRequest?): GetTrafficPolicyResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun deleteTrafficPolicyInstance(p0: DeleteTrafficPolicyInstanceRequest?): DeleteTrafficPolicyInstanceResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun getGeoLocation(p0: GetGeoLocationRequest?): GetGeoLocationResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun getGeoLocation(): GetGeoLocationResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun createTrafficPolicyInstance(p0: CreateTrafficPolicyInstanceRequest?): CreateTrafficPolicyInstanceResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun listVPCAssociationAuthorizations(p0: ListVPCAssociationAuthorizationsRequest?): ListVPCAssociationAuthorizationsResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun associateVPCWithHostedZone(p0: AssociateVPCWithHostedZoneRequest?): AssociateVPCWithHostedZoneResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun getTrafficPolicyInstance(p0: GetTrafficPolicyInstanceRequest?): GetTrafficPolicyInstanceResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun listTagsForResource(p0: ListTagsForResourceRequest?): ListTagsForResourceResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun getReusableDelegationSet(p0: GetReusableDelegationSetRequest?): GetReusableDelegationSetResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun changeTagsForResource(p0: ChangeTagsForResourceRequest?): ChangeTagsForResourceResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun setEndpoint(p0: String?) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun listTrafficPolicyInstancesByPolicy(p0: ListTrafficPolicyInstancesByPolicyRequest?): ListTrafficPolicyInstancesByPolicyResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun listTrafficPolicyVersions(p0: ListTrafficPolicyVersionsRequest?): ListTrafficPolicyVersionsResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun listResourceRecordSets(p0: ListResourceRecordSetsRequest?): ListResourceRecordSetsResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun listTrafficPolicies(p0: ListTrafficPoliciesRequest?): ListTrafficPoliciesResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun listTrafficPolicies(): ListTrafficPoliciesResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun listHostedZonesByName(p0: ListHostedZonesByNameRequest?): ListHostedZonesByNameResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun listHostedZonesByName(): ListHostedZonesByNameResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun deleteHostedZone(p0: DeleteHostedZoneRequest?): DeleteHostedZoneResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun updateHealthCheck(p0: UpdateHealthCheckRequest?): UpdateHealthCheckResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun getTrafficPolicyInstanceCount(p0: GetTrafficPolicyInstanceCountRequest?): GetTrafficPolicyInstanceCountResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun getTrafficPolicyInstanceCount(): GetTrafficPolicyInstanceCountResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun listHostedZones(p0: ListHostedZonesRequest?): ListHostedZonesResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun listHostedZones(): ListHostedZonesResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun setRegion(p0: Region?) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun createHealthCheck(p0: CreateHealthCheckRequest?): CreateHealthCheckResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun getHealthCheck(p0: GetHealthCheckRequest?): GetHealthCheckResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun createVPCAssociationAuthorization(p0: CreateVPCAssociationAuthorizationRequest?): CreateVPCAssociationAuthorizationResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun createReusableDelegationSet(p0: CreateReusableDelegationSetRequest?): CreateReusableDelegationSetResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun deleteHealthCheck(p0: DeleteHealthCheckRequest?): DeleteHealthCheckResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun getChange(p0: GetChangeRequest?): GetChangeResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun updateHostedZoneComment(p0: UpdateHostedZoneCommentRequest?): UpdateHostedZoneCommentResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun updateTrafficPolicyComment(p0: UpdateTrafficPolicyCommentRequest?): UpdateTrafficPolicyCommentResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun listGeoLocations(p0: ListGeoLocationsRequest?): ListGeoLocationsResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun listGeoLocations(): ListGeoLocationsResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun deleteVPCAssociationAuthorization(p0: DeleteVPCAssociationAuthorizationRequest?): DeleteVPCAssociationAuthorizationResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun listTagsForResources(p0: ListTagsForResourcesRequest?): ListTagsForResourcesResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun createTrafficPolicyVersion(p0: CreateTrafficPolicyVersionRequest?): CreateTrafficPolicyVersionResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun getCheckerIpRanges(p0: GetCheckerIpRangesRequest?): GetCheckerIpRangesResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun getCheckerIpRanges(): GetCheckerIpRangesResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun getHealthCheckStatus(p0: GetHealthCheckStatusRequest?): GetHealthCheckStatusResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun getHostedZoneCount(p0: GetHostedZoneCountRequest?): GetHostedZoneCountResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun getHostedZoneCount(): GetHostedZoneCountResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun listReusableDelegationSets(p0: ListReusableDelegationSetsRequest?): ListReusableDelegationSetsResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun listReusableDelegationSets(): ListReusableDelegationSetsResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun listTrafficPolicyInstances(p0: ListTrafficPolicyInstancesRequest?): ListTrafficPolicyInstancesResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun listTrafficPolicyInstances(): ListTrafficPolicyInstancesResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun disassociateVPCFromHostedZone(p0: DisassociateVPCFromHostedZoneRequest?): DisassociateVPCFromHostedZoneResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun getHealthCheckLastFailureReason(p0: GetHealthCheckLastFailureReasonRequest?): GetHealthCheckLastFailureReasonResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun getHostedZone(p0: GetHostedZoneRequest?): GetHostedZoneResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun waiters(): AmazonRoute53Waiters {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun deleteReusableDelegationSet(p0: DeleteReusableDelegationSetRequest?): DeleteReusableDelegationSetResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun getCachedResponseMetadata(p0: AmazonWebServiceRequest?): ResponseMetadata {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun createTrafficPolicy(p0: CreateTrafficPolicyRequest?): CreateTrafficPolicyResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun getHealthCheckCount(p0: GetHealthCheckCountRequest?): GetHealthCheckCountResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun getHealthCheckCount(): GetHealthCheckCountResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun listHealthChecks(p0: ListHealthChecksRequest?): ListHealthChecksResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun listHealthChecks(): ListHealthChecksResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun updateTrafficPolicyInstance(p0: UpdateTrafficPolicyInstanceRequest?): UpdateTrafficPolicyInstanceResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun changeResourceRecordSets(p0: ChangeResourceRecordSetsRequest?): ChangeResourceRecordSetsResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun shutdown() {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun listTrafficPolicyInstancesByHostedZone(p0: ListTrafficPolicyInstancesByHostedZoneRequest?): ListTrafficPolicyInstancesByHostedZoneResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun createHostedZone(p0: CreateHostedZoneRequest?): CreateHostedZoneResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun testDNSAnswer(p0: TestDNSAnswerRequest?): TestDNSAnswerResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun deleteTrafficPolicy(p0: DeleteTrafficPolicyRequest?): DeleteTrafficPolicyResult {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}
}